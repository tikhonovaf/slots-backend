package ru.ttk.slotsbe.backend.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ttk.slotsbe.backend.dto.ClientUserInDto;
import ru.ttk.slotsbe.backend.model.ClientUser;
import ru.ttk.slotsbe.backend.model.VClient;
import ru.ttk.slotsbe.backend.repository.VClientRepository;
import ru.ttk.slotsbe.backend.util.CoreUtil;

import java.util.List;

/**
 * Маппинг:
 * -  между view и dto rest сервиса
 * -  между dto rest сервиса и entity сущности
 */
@Service
public class ClientUserMapper {

    @Autowired
    private VClientRepository vClientRepository;

    /**
     * Маппингиз DTO в Entity
     *
     * @param dto - строка из DTO
     * @return данные в структуре Entity
     */
    public ClientUser fromDtoToEntity(ClientUserInDto dto) {
        ClientUser result = new ClientUser();
        CoreUtil.patch(dto, result);


        if (dto.getVcClientCode() != null) {
            List<VClient> vClients = vClientRepository.findAllByVcCode(dto.getVcClientCode());
            if (vClients.size() > 0) {
                result.setNClientId(vClients.get(0).getNClientId());
            }
        }

        return result;
    }
    
}
