package ru.ttk.slotsbe.backend.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ttk.slotsbe.backend.dto.Page;
import ru.ttk.slotsbe.backend.dto.*;
import ru.ttk.slotsbe.backend.model.ClientUser;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import ru.ttk.slotsbe.backend.service.rest.UserService;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

/**
 * Класс для выполнения выборок из БД с учетом фильтров и пагинации
 *
 * @author Гончаров Егор
 */
@Service
public class SearchService {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserService userService;

    /**
     * Определение значений объекта Page
     *
     * @param paging  - количество записей на странице выборки
     * @param pageDto - выборка для страницы
     * @return объект Page
     */
    public Page getPage(Paging paging, PageDto pageDto) {
        Long page;
        Long size;
        if (paging == null || paging.getNumber() == null) {
            page = Long.valueOf(DEFAULT_PAGE);
        } else {
            page = paging.getNumber();
        }
        if (paging == null || paging.getSize() == null) {
            size = Long.valueOf(DEFAULT_SIZE);
        } else {
            size = paging.getSize();
        }

        Page result = new Page();
        result.setNumber(page);
        result.setSize(size);
        result.setTotalElements(pageDto.getTotal());

        if (pageDto.getTotal() % size == 0) {
            result.setTotalPages(pageDto.getTotal() / size);
        } else {
            result.setTotalPages(pageDto.getTotal() / size + 1);
        }

        return result;
    }

    /**
     * Формирование выборки по заданному фильтру и параметрам сортировки
     *
     * @param paging - количество записей на странице выборки
     * @param search - фильтр
     * @param sort   - параметры сортировки
     * @param c      - класс enttity для выборки
     * @return объект PageDto
     */
    public PageDto findByFilter(Object search, Paging paging, List<SortColumn> sort, Class c) {
        List<SearchDto> searchDtoList = new ArrayList<>();

        Field[] fields = search.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = null;
            try {
                value = field.get(search);
            } catch (Exception e) {
                // do nothing - value is null
            }
            if (value != null) {
                value = value.toString();
                if (field.getName().toLowerCase().endsWith("id")) {
//                       Список идентификаторов элементов справочников
                    searchDtoList.add(new SearchDto(field.getName(), (String) value, SearchType.ID));
                } else if (field.getType().getTypeName().toLowerCase().contains("localdatetime")) {
//                       Сравнение фильтров для поля типа Дата + Время  (LocalDateTime)
                    if (field.getName().endsWith("To")) {
                        // Дата + Время  должна быть <= переданного значения
                        searchDtoList.add(new SearchDto(field.getName().substring(0, field.getName().length() - 2), (String) value, SearchType.LOCAL_DATE_TIME_TO));
                    } else if (field.getName().endsWith("From")) {
                        // Дата + Время  должна быть >= переданного значения
                        searchDtoList.add(new SearchDto(field.getName().substring(0, field.getName().length() - 4), (String) value, SearchType.LOCAL_DATE_TIME_FROM));
                    }
                } else if (field.getType().getTypeName().toLowerCase().contains("localdate")) {
//                       Сравнение фильтров для поля типа Дата (LocalDate)
                    if (field.getName().endsWith("To")) {
                        // Дата *date*end должна быть <= переданного значения
                        searchDtoList.add(new SearchDto(field.getName().substring(0, field.getName().length() - 2), (String) value, SearchType.LOCAL_DATE_TO));
                    } else if (field.getName().endsWith("From")) {
                        // Дата *date*end должна быть >= переданного значения
                        searchDtoList.add(new SearchDto(field.getName().substring(0, field.getName().length() - 4), (String) value, SearchType.LOCAL_DATE_FROM));
                    }
                } else if (field.getType().getTypeName().toLowerCase().contains("date")) {
//                       Сравнение фильтров для поля типа Дата
                    if (field.getName().endsWith("To")) {
                        // Дата *date*end должна быть <= переданного значения
                        searchDtoList.add(new SearchDto(field.getName().substring(0, field.getName().length() - 2), (String) value, SearchType.DATE_TO));
                    } else if (field.getName().endsWith("From")) {
                        // Дата *date*end должна быть >= переданного значения
                        searchDtoList.add(new SearchDto(field.getName().substring(0, field.getName().length() - 4), (String) value, SearchType.DATE_FROM));
                    }
                } else if (field.getType().getTypeName().toLowerCase().contains("long") ||
                        field.getType().getTypeName().toLowerCase().contains("integer")) {
//                       Сравнение фильтров для поля типа Число
                    if (field.getName().endsWith("To")) {
                        searchDtoList.add(new SearchDto(field.getName().substring(0, field.getName().length() - 2), (String) value, SearchType.NUMBER_TO));
                    } else if (field.getName().endsWith("From")) {
                        searchDtoList.add(new SearchDto(field.getName().substring(0, field.getName().length() - 4), (String) value, SearchType.NUMBER_FROM));
                    } else {
                        searchDtoList.add(new SearchDto(field.getName().substring(0, field.getName().length()), (String) value, SearchType.NUMBER_EQUAL));
                    }
//                       Сравнение фильтров для поля типа Строка
                } else if (field.getType().getTypeName().toLowerCase().contains("string")) {
                    if (field.getName().endsWith("List")) {
                        searchDtoList.add(new SearchDto(field.getName().substring(0, field.getName().length() - 4), (String) value, SearchType.STRING_LIST));
                    } else {
                        searchDtoList.add(new SearchDto(field.getName(), (String) value, SearchType.STRING));
                    }
                }
            }
        }
        return findByFilter(c, paging, sort, searchDtoList);
    }

    /**
     * Поиск по фильтру
     *
     * @param c          - класс
     * @param paging     - параметры пагинации
     * @param sort       - параметры сортировки
     * @param searchDtos - параметры поиска
     * @return страница
     */
    private PageDto findByFilter(Class c, Paging paging, List<SortColumn> sort, List<SearchDto> searchDtos) {
        Long page;
        Long size;
        if (paging == null || paging.getNumber() == null) {
            page = Long.valueOf(DEFAULT_PAGE);
        } else {
            page = paging.getNumber();
        }
        if (paging == null || paging.getSize() == null) {
            size = Long.valueOf(DEFAULT_SIZE);
        } else {
            size = paging.getSize();
        }

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery cQuery = cb.createQuery(c);
        Root from = cQuery.from(c);
        CriteriaQuery select = cQuery.select(from);

        // Conditions
        Predicate predicate = cb.conjunction();
        for (SearchDto searchDto : searchDtos) {
            if (searchDto.getValue() != null) {
                if (searchDto.getType() == SearchType.STRING) {
                    predicate = cb.and(predicate, cb.like(cb.lower(from.get(searchDto.getField())), cb.lower(cb.literal("%" + searchDto.getValue() + "%"))));
                }

                if (searchDto.getType() == SearchType.DATE_FROM) {
                    //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    //LocalDate date = LocalDate.parse(searchDto.getValue(), formatter);
                    Date date;
                    try {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        date = formatter.parse(searchDto.getValue());
                    } catch (ParseException e) {
                        e.printStackTrace();
                        throw new RuntimeException("Некорректный формат даты " + searchDto.getValue());
                    }

                    Predicate p1 = cb.disjunction();
                    p1 = cb.or(p1, cb.greaterThanOrEqualTo(from.get(searchDto.getField()), date));
//                    p1 = cb.or(p1, cb.isNull(from.get(searchDto.getField())));
                    predicate = cb.and(predicate, p1);
                }

                if (searchDto.getType() == SearchType.DATE_TO) {
                    //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    //LocalDate date = LocalDate.parse(searchDto.getValue(), formatter);
                    Date date;
                    try {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        date = formatter.parse(searchDto.getValue());
                    } catch (ParseException e) {
                        e.printStackTrace();
                        throw new RuntimeException("Некорректный формат даты " + searchDto.getValue());
                    }
                    Predicate p1 = cb.disjunction();
                    p1 = cb.or(p1, cb.lessThanOrEqualTo(from.get(searchDto.getField()), date));
//                    p1 = cb.or(p1, cb.isNull(from.get(searchDto.getField())));
                    predicate = cb.and(predicate, p1);
                }

                if (searchDto.getType() == SearchType.LOCAL_DATE_FROM) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate date = LocalDate.parse(searchDto.getValue(), formatter);
                    Predicate p1 = cb.disjunction();
                    p1 = cb.or(p1, cb.greaterThanOrEqualTo(from.get(searchDto.getField()), date));
//                    p1 = cb.or(p1, cb.isNull(from.get(searchDto.getField())));
                    predicate = cb.and(predicate, p1);
                }

                if (searchDto.getType() == SearchType.LOCAL_DATE_TO) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate date = LocalDate.parse(searchDto.getValue(), formatter);
                    Predicate p1 = cb.disjunction();
                    p1 = cb.or(p1, cb.lessThanOrEqualTo(from.get(searchDto.getField()), date));
//                    p1 = cb.or(p1, cb.isNull(from.get(searchDto.getField())));
                    predicate = cb.and(predicate, p1);
                }

                if (searchDto.getType() == SearchType.LOCAL_DATE_TIME_FROM) {
                    DateTimeFormatter formatter = ISO_LOCAL_DATE_TIME;
                    LocalDateTime dateTime = LocalDateTime.parse(searchDto.getValue(), formatter);
                    Predicate p1 = cb.disjunction();
                    p1 = cb.or(p1, cb.greaterThanOrEqualTo(from.get(searchDto.getField()), dateTime));
//                    p1 = cb.or(p1, cb.isNull(from.get(searchDto.getField())));
                    predicate = cb.and(predicate, p1);
                }

                if (searchDto.getType() == SearchType.LOCAL_DATE_TIME_TO) {
                    DateTimeFormatter formatter = ISO_LOCAL_DATE_TIME;
                    LocalDateTime dateTime = LocalDateTime.parse(searchDto.getValue(), formatter);
                    Predicate p1 = cb.disjunction();
                    p1 = cb.or(p1, cb.lessThanOrEqualTo(from.get(searchDto.getField()), dateTime));
//                    p1 = cb.or(p1, cb.isNull(from.get(searchDto.getField())));
                    predicate = cb.and(predicate, p1);
                }
                if (searchDto.getType() == SearchType.NUMBER_FROM) {
                    Double number = Double.valueOf(searchDto.getValue());
                    Predicate p1 = cb.disjunction();
                    p1 = cb.or(p1, cb.greaterThanOrEqualTo(from.get(searchDto.getField()), number));
//                    p1 = cb.or(p1, cb.isNull(from.get(searchDto.getField())));
                    predicate = cb.and(predicate, p1);
                }

                if (searchDto.getType() == SearchType.NUMBER_TO) {
                    Double number = Double.valueOf(searchDto.getValue());
                    Predicate p1 = cb.disjunction();
                    p1 = cb.or(p1, cb.lessThanOrEqualTo(from.get(searchDto.getField()), number));
//                    p1 = cb.or(p1, cb.isNull(from.get(searchDto.getField())));
                    predicate = cb.and(predicate, p1);
                }

                if (searchDto.getType() == SearchType.NUMBER_EQUAL) {
                    Double number = Double.valueOf(searchDto.getValue());
                    Predicate p1 = cb.disjunction();
                    p1 = cb.or(p1, cb.equal(from.get(searchDto.getField()), number));
//                    p1 = cb.or(p1, cb.isNull(from.get(searchDto.getField())));
                    predicate = cb.and(predicate, p1);
                }

                if (searchDto.getType() == SearchType.ID) {
                    List<Long> ids = Arrays.stream(searchDto.getValue().split(","))
                            .map(s -> Long.valueOf(s.trim())).collect(Collectors.toList());
                    Predicate p1 = cb.disjunction();
                    for (Long id : ids) {
                        p1 = cb.or(p1, cb.equal(from.get(searchDto.getField()), id));
                    }
                    predicate = cb.and(predicate, p1);
                }
                if (searchDto.getType() == SearchType.STRING_LIST) {
                    List<String> strList = Arrays.stream(searchDto.getValue().split(","))
                            .map(s -> s.trim())
                            .collect(Collectors.toList());
                    Predicate p1 = cb.disjunction();
                    for (String str : strList) {
                        p1 = cb.or(p1, cb.equal(from.get(searchDto.getField()), str));
                    }
                    predicate = cb.and(predicate, p1);
                }
            }
        }
        // --

        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        cq.select(cb.count(cq.from(c)));
        entityManager.createQuery(cq);
        cq.where(predicate);

        // Sort
        if (sort != null && !sort.isEmpty()) {
            List<Order> orderList = new ArrayList<>();
            for (SortColumn sortColumn : sort) {
                if (sortColumn.getSortDirection() != null && sortColumn.getSortDirection().trim().toLowerCase().equals("desc")) {
                    orderList.add(cb.desc(from.get(sortColumn.getColumnName())));
                } else {
                    orderList.add(cb.asc(from.get(sortColumn.getColumnName())));
                }
            }
            cQuery.orderBy(orderList);
        }
        // --

        Long total = entityManager.createQuery(cq).getSingleResult();
        select.where(predicate);

        TypedQuery typedQuery = entityManager.createQuery(select);
        typedQuery.setFirstResult((int) (page * size));

        typedQuery.setMaxResults(size.intValue());
        List elements = typedQuery.getResultList();

        PageDto result = new PageDto();
        result.setElements(elements);
        result.setTotal(total);

        return result;
    }

    /**
     * Определение текущего языка
     */
    public String getAppLang() {
        String result = "RU";
        String userLang = "";
        String varLang = "";

        //  Сначала определяем язык пользователя
        ClientUser user = userService.getCurrentUser();
//        if (user != null && user.getLang() != null) {
//            userLang = user.getLang().toUpperCase();
//        }

        // Определяем язык приложения
//        varLang = lang.toUpperCase();
//        if (lang.toUpperCase().contains("EN")) {
//            varLang = "EN";
//        }

//        if (!userLang.isEmpty()) {
//            result = userLang;
//        } else if (!varLang.isEmpty()) {
//            result = varLang;
//        }
        return result;
    }

    /**
     * Определение значения константы с учетом текущего языка
     */
    public String getConstLang(String constCode) {

//        List<ConstLang> result = constLangRepository.findAllByConstCodeAndLang(constCode, getAppLang());
//        if (result.size() > 0) {
//            return result.get(0).getLangName();
//        } else {
//            return constCode;
//        }
        return null;
    }


}
