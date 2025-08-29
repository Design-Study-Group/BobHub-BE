package com.bobhub.party.domain;

import lombok.Getter;

@Getter
public enum PartyCategory {
  DELIVERY("배달"),
  DINE_OUT("외식"),
  LUNCHBOX("도시락"),
  BETTING("내기");

  private final String krName;

  PartyCategory(String krName) {
    this.krName = krName;
  }

}
