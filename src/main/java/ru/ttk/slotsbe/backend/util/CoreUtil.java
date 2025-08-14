package ru.ttk.slotsbe.backend.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Класс общих функций и констант
 *
 * @author Гончаров Егор
 */

@Slf4j
public class CoreUtil<result> {

//    ======================  slot_type  ==========================
    /**
     * Тип openstack
     */
    public static final String CLUSTER_TYPE_OPENSTACK = "openstack";

    /**
     * Тип vmware
     */
    public static final String CLUSTER_TYPE_VMWARE = "vmware";

    /**
     * Тип ovirt
     */
    public static final String CLUSTER_TYPE_OVIRT = "ovirt";


    //    ======================  report type  ==========================

    /**
     * Тип отчета VM
     */
    public static final String REPORT_TYPE_VM = "VM";

    /**
     * Тип отчета VMDISK
     */
    public static final String REPORT_TYPE_VMDISK = "VMDISK";

    //    ======================  report format  ==========================

    /**
     * Формат отчета JSON
     */
    public static final String REPORT_FORMAT_JSON = "JSON";

    /**
     * Формат отчета CSV
     */
    public static final String REPORT_FORMAT_CSV = "CSV";

    //    ======================  storage_type  ==========================
    /**
     * Тип CEPH
     */
    public static final String STORAGE_TYPE_CEPH = "CEPH";
    /**
     * Тип Local
     */
    public static final String STORAGE_TYPE_LOCAL = "Local";
    /**
     * //     ==================  migration config file types ===========================================
     * /**
     * Тип  Список хостов копирования
     */
    public static final String HOSTS_LIST = "hostsList";

    /**
     * Тип Маппинг сетей
     */
    public static final String NET_MAPPING = "netMapping";

    /**
     * Тип Маппинг проектов/групп
     */
    public static final String PROJECT_MAPPING = "projectMapping";

    /**
     * Тип Миграция по списку ВМ
     */
    public static final String VM_LIST = "vmList";
    /**
     * Тип Маппинг шаблонов ВМ
     */
    public static final String VM_TEMPLATES_MAPPING = "vmTemplatesMapping";


    /**
     * Тип Маппинг шаблонов    ВМ
     */
    public static final String CLUSTER_IN_VAL_SCRIPT = "slotInValScriptFile";

    /**
     * Тип Маппинг шаблонов ВМ
     */
    public static final String CLUSTER_OUT_VAL_SCRIPT = "slotOutValScriptFile";


    /**
     * Тип Конфигурационный файл для кластера
     */
    public static final String CONF_CLUSTER = "slot";

    /**
     * Тип Конфигурационный файл   для CEPH кластера
     */
    public static final String CONF_CEPH = "CEPH";

    /**
     * Тип Конфигурационный файл для user_keyring
     */
    public static final String USER_KEYRING = "user_keyring";

    public static final String OPENSTACK_SSH_PRIVATE_KEY = "openstackSshPrivateKey";


    /**
     * Типы метрики миграции ВМ
     */
    //   Скорость передачи
    public static final Long METRIC_COPY_SPEED = 3l;
    //   Время запуска миграции ВМ
    public static final Long METRIC_START_TIME = 4l;
    //   Время завершения миграции ВМ
    public static final Long METRIC_FINISH_TIME = 5l;
    //   Время остановки миграции ВМ
    public static final Long METRIC_PAUSE_TIME = 6l;
    //   Время запуска миграции ВМ
    public static final Long METRIC_RESUME_TIME = 7l;
    //   Время простоя миграции ВМ
    public static final Long METRIC_IDLE_TIME = 8l;


//      ---- =================  Общее =============== ------------------------

    /**
     * Маппинг атрибутов объекта одного класса в атрибуты объекта другого класса
     *
     * @param obj    Исходный объект
     * @param newObj Объект, значения атрибутов которого определяется
     */
    public static void patch(Object newObj, Object obj) {

        if (newObj == null) {
            obj = null;
            return;
        }

        Class cBase = obj.getClass();
        Class cNew = newObj.getClass();


        List<Field> fieldsBase = new ArrayList<>();
        getFieldsUpTo(cBase, fieldsBase);
        List<Field> fieldsNew = new ArrayList<>();
        getFieldsUpTo(cNew, fieldsNew);
        for (Field fieldBase : fieldsBase) {
            for (Field fieldNew : fieldsNew) {
                if (fieldBase.getName().equals(fieldNew.getName()) && !fieldBase.getName().equals("serialVersionUID")) {
                    try {
                        fieldBase.setAccessible(true);
                        fieldNew.setAccessible(true);
                    } catch (Exception e) {
                        log.info("===  patch  -  fieldBase.getName() = " + fieldBase.getName());
                    }
                    try {
                        Object value = fieldNew.get(newObj);
                        if (value != null) {
                            try {
                                fieldBase.set(obj, value);
                            } catch (IllegalArgumentException e) {
                                try {
                                    Object valueToSet = fieldBase.getType().newInstance();
                                    patch(value, valueToSet);
                                    fieldBase.set(obj, valueToSet);
                                } catch (InstantiationException ex) {
                                    // must be created always
                                }
                            }
//                        } else {
//                            fieldBase.set(obj, null);
                        }
                    } catch (IllegalAccessException e) {
                        // never catched because of setAccessible(true)
                    }
                }
            }
        }
    }

    /**
     * Уточнение списка полей класса
     *
     * @param startClass - класс
     * @param fields     - массив полей
     */
    public static void getFieldsUpTo(Class<?> startClass, List<Field> fields) {
        fields.addAll(Arrays.asList(startClass.getDeclaredFields()));

        Class<?> parentClass = startClass.getSuperclass();
        if (parentClass != null) {
            getFieldsUpTo(parentClass, fields);
        }
    }

    public static LocalDateTime getZoneCurrentDateTime(String timeZone) {
        //              Определяем локальное время
        ZonedDateTime timeZoneDateTimeNow = LocalDateTime.now().atZone(ZoneId.of(timeZone));
        ZonedDateTime timeZoneDateTimeLondon = LocalDateTime.now().atZone(ZoneId.of("Europe/London"));
        LocalDateTime localDateTimeForTimeZone = timeZoneDateTimeLondon.toLocalDateTime().plusSeconds(timeZoneDateTimeNow.getOffset().getTotalSeconds() -
                ZonedDateTime.now(ZoneId.systemDefault()).getOffset().getTotalSeconds());
        return localDateTimeForTimeZone;
    }


}
