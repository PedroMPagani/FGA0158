package org.unbbrasilia.fga0158.component;

import lombok.Getter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public class Park {

    private final int parkingId;
    private final String localZone;
    private final List<Access> carsList;
    private final int maxCarCapacity;

    // O estacionamento 1 tem capacidade de 300 veiculos, funciona de 6:00 às 22:00 horas, tem valor de fração de R$30,00, com um desconto de 15% para cada hora cheia, e R$120,00 de diária diurna.
    // O estacioamento 2 tem capacidade de 120 veiculos, funciona 24 horas, tem valor de fração de R$20,00, com um desconto de 10% para cada hora cheia e R$70,00 de diária noturna.

    public Park(int parkId, String localZone, CopyOnWriteArrayList<Access> list, int maxCarCapacity){
        this.parkingId = parkId;
        this.localZone = localZone;
        this.carsList = list;
        this.maxCarCapacity = maxCarCapacity;
    }

    public Park(int parkId, String localZone, int maxCarCapacity){
        this(parkId,localZone,new CopyOnWriteArrayList<Access>(),maxCarCapacity);
    }

}