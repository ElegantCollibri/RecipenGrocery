package com.ppl6.recipengrocery.util;

import android.util.Log;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Units {

  // - - - - - - - - - - - - - - - Private constructor - - - - - - - - - - - - - - -

  private Units(){
    throw new RuntimeException("DO NOT INSTANTIATE!");
  }


  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private static final String LOG_TAG = "GGG - Units";

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

  public enum Misc {
    DROPS,
    NO_UNIT,
    PINCH;
  }

  private static final BiMap<String, Volume> volumeBiMap = HashBiMap.create();
  private static final BiMap<String, Mass> massBiMap = HashBiMap.create();
  private static final BiMap<String, Misc> miscBiMap = HashBiMap.create();

  static {
    volumeBiMap.put("ml", Volume.MILLILITRES);
    volumeBiMap.put("cups (AU)", Volume.AU_CUPS);
    volumeBiMap.put("tsp (AU)", Volume.AU_TEASPOONS);
    volumeBiMap.put("tbsp (AU)", Volume.AU_TABLESPOONS);
    volumeBiMap.put("cups (US)", Volume.US_CUPS);
    volumeBiMap.put("flOz", Volume.FLUID_OUNCES);
    volumeBiMap.put("qt", Volume.QUARTS);
    volumeBiMap.put("tsp (US)", Volume.US_TEASPOONS);
    volumeBiMap.put("tbsp (US)", Volume.US_TABLESPOONS);

    massBiMap.put("g", Mass.GRAMS);
    massBiMap.put("kg", Mass.KILOGRAMS);
    massBiMap.put("lbs", Mass.POUNDS);
    massBiMap.put("Oz", Mass.OUNCES);

    miscBiMap.put("drops", Misc.DROPS);
    miscBiMap.put("x", Misc.NO_UNIT);
    miscBiMap.put("pinch", Misc.PINCH);
  }

  // - - - - - - - - - - - - - - - Volume - - - - - - - - - - - - - - -

  public static Volume getVolumeFromUiString(String uiString) {
    return volumeBiMap.get(uiString);
  }

  private static Volume getVolumeFromVolumeName(String volumeName) {
    try {
      return Volume.valueOf(volumeName);

    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  private static String getUiStringFromVolumeName(String volumeName) {
    try {
      Volume volume = Volume.valueOf(volumeName);
      return volumeBiMap.inverse().get(volume);

    } catch (IllegalArgumentException e) {
      return null;
    }
  }


  // - - - - - - - - - - - - - - - Mass - - - - - - - - - - - - - - -

  public static Mass getMassFromUiString(String uiString) {
    return massBiMap.get(uiString);
  }

  private static Mass getMassFromMassName(String massName) {
    try {
      return Mass.valueOf(massName);

    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  private static String getUiStringFromMassName(String massName) {
    try {
      Mass mass = Mass.valueOf(massName);
      return massBiMap.inverse().get(mass);

    } catch (IllegalArgumentException e) {
      return null;
    }
  }


  // - - - - - - - - - - - - - - - Misc - - - - - - - - - - - - - - -

  public static Misc getMiscFromUiString(String uiString) {
    return miscBiMap.get(uiString);
  }

  private static Misc getMiscFromMiscName(String miscName) {
    try {
      return Misc.valueOf(miscName);

    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  private static String getUiStringFromMiscName(String miscName) {
    try {
      Misc misc = Misc.valueOf(miscName);
      return miscBiMap.inverse().get(misc);

    } catch (IllegalArgumentException e) {
      return null;
    }
  }


  // - - - - - - - - - - - - - - - Any-unit - - - - - - - - - - - - - - -

  public static String getNameFromUiString(String uiString) {
    if (volumeBiMap.containsKey(uiString)) {
      return volumeBiMap.get(uiString).name();
    }
    else if (massBiMap.containsKey(uiString)){
      return massBiMap.get(uiString).name();
    }
    else if (miscBiMap.containsKey(uiString)) {
      return miscBiMap.get(uiString).name();
    } else {
      return null;
    }
  }

  public static String getUiStringFromName(String name) {
    if (getUiStringFromVolumeName(name) != null) {
      return getUiStringFromVolumeName(name);

    } else if (getUiStringFromMassName(name) != null) {
      return getUiStringFromMassName(name);

    } else if (getUiStringFromMiscName(name) != null) {
      return getUiStringFromMiscName(name);

    } else {
      return Misc.NO_UNIT.name();
    }
  }

  public static String getNameFromName(String name) {
    if (getVolumeFromVolumeName(name) != null) {
      return getVolumeFromVolumeName(name).name();

    } else if (getMassFromMassName(name) != null) {
      return getMassFromMassName(name).name();

    } else if (getMiscFromMiscName(name) != null) {
      return getMiscFromMiscName(name).name();

    } else {
      return Misc.NO_UNIT.name();
    }
  }

  public static List<String> getUnitList() {
    List<String> unitList = new ArrayList<>();
    unitList.addAll(volumeBiMap.keySet());
    unitList.addAll(massBiMap.keySet());
    unitList.addAll(miscBiMap.keySet());
    return unitList;
  }


  // - - - - - - - - - - - - - - - Formatting - - - - - - - - - - - - - - -

  private static String formatForDetailView(double amount, Volume volume, boolean isMetric) {
    Volume newVolume;

    if(isMetric) {
      switch (volume) {
        case US_CUPS:
          newVolume = Volume.AU_CUPS;
          break;
        case FLUID_OUNCES:
          newVolume = Volume.MILLILITRES;
          break;
        case QUARTS:
          newVolume = Volume.AU_CUPS;
          break;
        case US_TEASPOONS:
          newVolume = Volume.AU_TEASPOONS;
          break;
        case US_TABLESPOONS:
          newVolume = Volume.AU_TABLESPOONS;
          break;
        default:
          newVolume = volume;
      }
    } else {
      switch (volume) {
        case MILLILITRES:
          newVolume = Volume.FLUID_OUNCES;
          break;
        case AU_CUPS:
          newVolume = Volume.US_CUPS;
          break;
        case AU_TEASPOONS:
          newVolume = Volume.US_TEASPOONS;
          break;
        case AU_TABLESPOONS:
          newVolume = Volume.US_TABLESPOONS;
          break;
        default:
          newVolume = volume;
      }
    }
    return Units.convertVolume(amount, volume, newVolume);
  }

  private static String formatForDetailView(double amount, Mass mass, boolean isMetric) {

    Mass newMass;

    if (isMetric) {
      switch (mass) {
        case OUNCES:
          newMass = Mass.GRAMS;
          break;
        case POUNDS:
          newMass = Mass.GRAMS;
          break;
        default:
          newMass = mass;
      }

    } else {
      switch (mass) {
        case GRAMS:
          newMass = Mass.OUNCES;
          break;
        case KILOGRAMS:
          newMass = Mass.POUNDS;
          break;
        default:
          newMass = mass;
      }
    }
    return Units.convertMass(amount, mass, newMass);
  }

  private static String formatForDetailView(double amount, Misc unit) {
    String amountString = String.format(Locale.getDefault(), "%.1f", amount);
    String unitString = miscBiMap.inverse().get(unit);
    return amountString + " " + unitString + " ";
  }

  public static String formatForDetailView(double amount, String unitName, boolean isMetric) {

    if (getVolumeFromVolumeName(unitName) != null) {
      return formatForDetailView(amount, getVolumeFromVolumeName(unitName), isMetric);

    } else if (getMassFromMassName(unitName) != null) {
      return formatForDetailView(amount, getMassFromMassName(unitName), isMetric);

    } else if (getMiscFromMiscName(unitName) != null) {
      return formatForDetailView(amount, getMiscFromMiscName(unitName));
    }
    Log.d(LOG_TAG,unitName + " not recognised as a Unit name.");
    return null;
  }

  public static boolean getIsMetric(String default_units) {
    String countryCode = Locale.getDefault().getCountry();
    boolean isImperialCountry =
        countryCode.equals("US") || countryCode.equals("LR") || countryCode.equals("MM");

    if (default_units.equals("imperial")
        || (default_units.equals("automatic") && isImperialCountry)) {
      return false;

    } else {
      return true;
    }
  }


  // - - - - - - - - - - - - - - - Helpers - - - - - - - - - - - - - - -

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

    String unit = volumeBiMap.inverse().get(to);

    if (to == Volume.MILLILITRES) {
      format = "%.0f";
    } else {
      format = "%.2f";
    }

    String newAmountString = String.format(Locale.getDefault(), format, newAmount);
    return newAmountString + " " + unit + " ";
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

    String unit = massBiMap.inverse().get(to);

    if (to == Mass.GRAMS) {
      format = "%.0f";

    } else if (to == Mass.KILOGRAMS || to == Mass.POUNDS) {
      format = "%.2f";

    } else {
      format = "%.1f";
    }

    String newAmountString = String.format(Locale.getDefault(), format, newAmount);
    return newAmountString + " " + unit + " ";
  }


  // - - - - - - - - - - - - - - - Conversion - - - - - - - - - - - - - - -

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
}
