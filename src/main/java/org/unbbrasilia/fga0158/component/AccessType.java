package org.unbbrasilia.fga0158.component;

import lombok.Getter;

public enum AccessType {

 MONTLY("Mensal"),
 NIGHT("Noturno"),
 EVENT("Evento");

 @Getter private final String name;

 AccessType(String name){
  this.name = name;
 }

}