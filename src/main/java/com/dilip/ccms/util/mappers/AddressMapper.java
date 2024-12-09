package com.dilip.ccms.util.mappers;

import com.dilip.ccms.address.Address;
import com.dilip.ccms.address.AddressDTO;
import com.dilip.ccms.address.City;
import com.dilip.ccms.address.State;

public class AddressMapper {
    public static Address toEntity(AddressDTO dto, State state, City city) {
        return Address.builder()
                .street(dto.getStreet())
                .city(city)
                .state(state)
                .build();
    }

    public static AddressDTO toDto(Address address) {
        return AddressDTO.builder()
                .street(address.getStreet())
                .cityID(address.getCity().getId())
                .stateID(address.getState().getId())
                .build();
    }
}
