package com.gerardbradshaw.tomatoes.helpers;

import android.util.Pair;

import java.util.Locale;
import java.util.Map;

public class Units {

  static boolean isMetric = true;  // TODO add this to shared preferences


  // - - - - - - - - - - - - - - - Private constructor - - - - - - - - - - - - - - -

  private Units(){
    throw new RuntimeException("DO NOT INSTANTIATE!");
  }


  // - - - - - - - - - - - - - - - Public methods - - - - - - - - - - - - - - -

  public static Volume getVolumeEnum(String unit) {
    for (Volume volumeValue : Volume.values()) {
      if (volumeValue.name().equals(unit)) {
        return volumeValue;
      }
    }

    return null;
  }

  public static Mass getMassEnum(String unit) {
    for (Mass massValue : Mass.values()) {
      if (massValue.name().equals(unit)) {
        return massValue;
      }
    }
    return null;
  }

  public static MiscUnits getMiscUnitsEnum(String unit) {
    for(MiscUnits miscUnitsValue : MiscUnits.values()) {
      if(miscUnitsValue.name().equals(unit)) {
        return miscUnitsValue;
      }
    }
    return null;
  }

  public static String formatForDetailView(double amount, Volume unit) {
    Volume newUnit;

    if(isMetric) {
      switch (unit) {
        case US_CUPS:
          newUnit = Volume.AU_CUPS;
          break;
        case FLUID_OUNCES:
          newUnit = Volume.MILLILITRES;
          break;
        case QUARTS:
          newUnit = Volume.AU_CUPS;
          break;
        case US_TEASPOONS:
          newUnit = Volume.AU_TEASPOONS;
          break;
        case US_TABLESPOONS:
          newUnit = Volume.AU_TABLESPOONS;
          break;
        default:
          newUnit = unit;
      }
    } else {
      switch (unit) {
        case MILLILITRES:
          newUnit = Volume.FLUID_OUNCES;
          break;
        case AU_CUPS:
          newUnit = Volume.US_CUPS;
          break;
        case AU_TEASPOONS:
          newUnit = Volume.US_TEASPOONS;
          break;
        case AU_TABLESPOONS:
          newUnit = Volume.US_TABLESPOONS;
          break;
        default:
          newUnit = unit;
      }
    }
    return Units.convertVolume(amount, unit, newUnit);
  }

  public static String formatForDetailView(double amount, Mass unit) {

    Mass newUnit;

    if (isMetric) {
      switch (unit) {
        case OUNCES:
          newUnit = Mass.GRAMS;
          break;
        case POUNDS:
          newUnit = Mass.GRAMS;
          break;
        default:
          newUnit = unit;
      }

    } else {
      switch (unit) {
        case GRAMS:
          newUnit = Mass.OUNCES;
          break;
        case KILOGRAMS:
          newUnit = Mass.POUNDS;
          break;
        default:
          newUnit = unit;
      }
    }
    return Units.convertMass(amount, unit, newUnit);
  }

  public static String formatForDetailView(double amount, MiscUnits unit) {
    String amountString = String.format(Locale.getDefault(), "%.1f", amount);
    switch (unit) {
      case DROPS:
        return amountString + " drops ";
      case PINCH:
        return amountString + " pinch ";
      default:
        return amountString + "x ";
    }
  }

  public static String formatForDetailView(double amount, String unit) {

    if (getVolumeEnum(unit) != null) {
      return formatForDetailView(amount, getVolumeEnum(unit));

    } else if (getMassEnum(unit) != null) {
      return formatForDetailView(amount, getMassEnum(unit));

    } else if (getMiscUnitsEnum(unit) != null) {
      return formatForDetailView(amount, getMiscUnitsEnum(unit));
    }
    return "Units error";
  }


  // - - - - - - - - - - - - - - - Private methods - - - - - - - - - - - - - - -

  /**
   * Converts a volume between units.
   *
   * @param amount the absolute amount in the original unit
   * @param from the original unit
   * @param to the new unit
   * @return the volume expressed in the new unit
   */
  private static String convertVolume(double amount, Volume from, Volume to) {

    double mlAmount = convertToMl(amount, from);
    double newAmount = convertFromMl(mlAmount, to);
    String format;

    String unit;

    switch (to) {
      case MILLILITRES:
        unit = "mL ";
        format = "%.0f";
        break;
      case AU_CUPS:
        unit = " cups ";
        format = "%.2f";
        break;
      case AU_TEASPOONS:
        unit = " tsp ";
        format = "%.2f";
        break;
      case AU_TABLESPOONS:
        unit = " tbsp ";
        format = "%.2f";
        break;
      case US_CUPS:
        unit = " cups ";
        format = "%.2f";
        break;
      case FLUID_OUNCES:
        unit = "flOz ";
        format = "%.2f";
        break;
      case QUARTS:
        unit = "qt ";
        format = "%.2f";
        break;
      case US_TEASPOONS:
        unit = " tsp (US) ";
        format = "%.2f";
        break;
      case US_TABLESPOONS:
        unit = " tbsp (US) ";
        format = "%.2f";
        break;
      default:
        unit = "x ";
        format = "%.1f";
    }

    String newAmountString = String.format(Locale.getDefault(), format, newAmount);
    return newAmountString + unit;

  }

  /**
   * Converts a mass between units.
   *
   * @param amount the absolute amount in the original unit
   * @param from the original unit
   * @param to the new unit
   * @return the mass expressed in the new unit
   */
  private static String convertMass(double amount, Mass from, Mass to) {

    double gramAmount = convertToGrams(amount, from);
    double newAmount = convertFromGrams(gramAmount, to);
    String format;

    String unit;

    switch (to) {
      case GRAMS:
        unit = "g ";
        format = "%.0f";
        break;
      case KILOGRAMS:
        unit = "kgs ";
        format = "%.2f";
        break;
      case OUNCES:
        unit = "oz ";
        format = "%.1f";
        break;
      case POUNDS:
        unit = "lbs ";
        format = "%.2f";
        break;
      default:
        unit = "x ";
        format = "%.1f";
    }

    String newAmountString = String.format(Locale.getDefault(), format, newAmount);
    return newAmountString + unit;
  }

  private static double convertToMl(double amount, Volume originalUnit) {
    switch (originalUnit) {
      case MILLILITRES:
        return amount;
      case AU_CUPS:
        return amount * 250;
      case AU_TEASPOONS:
        return amount * 5;
      case AU_TABLESPOONS:
        return amount * 20;
      case US_CUPS:
        return amount * 236.588;
      case FLUID_OUNCES:
        return amount * 29.574;
      case QUARTS:
        return amount * 946.353;
      case US_TEASPOONS:
        return amount * 4.92892;
      case US_TABLESPOONS:
        return amount * 14.7868;
      default:
        return 0;
    }
  }

  private static double convertFromMl(double amount, Volume desiredUnit) {
    switch (desiredUnit) {
      case MILLILITRES:
        return amount;
      case AU_CUPS:
        return amount / 250;
      case AU_TEASPOONS:
        return amount / 5;
      case AU_TABLESPOONS:
        return amount / 20;
      case US_CUPS:
        return amount / 236.588;
      case FLUID_OUNCES:
        return amount / 29.574;
      case QUARTS:
        return amount / 946.353;
      case US_TEASPOONS:
        return amount / 4.92892;
      case US_TABLESPOONS:
        return amount / 14.7868;
      default:
        return 0;
    }
  }

  private static double convertToGrams(double amount, Mass originalUnit) {
    switch (originalUnit) {
      case GRAMS:
        return amount;
      case KILOGRAMS:
        return amount * 1000;
      case OUNCES:
        return amount * 28.3495;
      case POUNDS:
        return amount * 453.592;
      default:
        return 0;
    }
  }

  private static double convertFromGrams(double amount, Mass desiredUnit) {
    switch (desiredUnit) {
      case GRAMS:
        return amount;
      case KILOGRAMS:
        return amount / 1000;
      case OUNCES:
        return amount / 28.3495;
      case POUNDS:
        return amount / 453.592;
      default:
        return 0;
    }
  }


  // - - - - - - - - - - - - - - - Enums - - - - - - - - - - - - - - -

  public enum Volume {
    MILLILITRES,
    AU_CUPS,
    AU_TEASPOONS,
    AU_TABLESPOONS,
    US_CUPS,
    FLUID_OUNCES,
    QUARTS,
    US_TEASPOONS,
    US_TABLESPOONS;
  }

  public enum Mass {
    GRAMS,
    KILOGRAMS,
    POUNDS,
    OUNCES;
  }

  public enum MiscUnits {
    DROPS,
    NO_UNIT,
    PINCH;
  }

}