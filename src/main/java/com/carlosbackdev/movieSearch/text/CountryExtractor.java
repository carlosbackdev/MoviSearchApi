
package com.carlosbackdev.movieSearch.text;

import java.util.*;

public class CountryExtractor {
    
private static final Map<String, String[]> COUNTRY_MAP = new HashMap<>();
    static {
        COUNTRY_MAP.put("AF", new String[]{"Afghanistan", "Afghan"});
        COUNTRY_MAP.put("AL", new String[]{"Albania", "Albanian"});
        COUNTRY_MAP.put("DZ", new String[]{"Algeria", "Algerian"});
        COUNTRY_MAP.put("AD", new String[]{"Andorra", "Andorran"});
        COUNTRY_MAP.put("AO", new String[]{"Angola", "Angolan"});
        COUNTRY_MAP.put("AR", new String[]{"Argentina", "Argentine"});
        COUNTRY_MAP.put("AM", new String[]{"Armenia", "Armenian"});
        COUNTRY_MAP.put("AU", new String[]{"Australia", "Australian"});
        COUNTRY_MAP.put("AT", new String[]{"Austria", "Austrian"});
        COUNTRY_MAP.put("BH", new String[]{"Bahrain", "Bahraini"});
        COUNTRY_MAP.put("BD", new String[]{"Bangladesh", "Bangladeshi"});
        COUNTRY_MAP.put("BY", new String[]{"Belarus", "Belarusian"});
        COUNTRY_MAP.put("BE", new String[]{"Belgium", "Belgian"});
        COUNTRY_MAP.put("BZ", new String[]{"Belize", "Belizean"});
        COUNTRY_MAP.put("BT", new String[]{"Bhutan", "Bhutanese"});
        COUNTRY_MAP.put("BO", new String[]{"Bolivia", "Bolivian"});
        COUNTRY_MAP.put("BA", new String[]{"Bosnia and Herzegovina", "Bosnian"});
        COUNTRY_MAP.put("BW", new String[]{"Botswana", "Botswanan"});
        COUNTRY_MAP.put("BR", new String[]{"Brazil", "Brazilian"});
        COUNTRY_MAP.put("BN", new String[]{"Brunei", "Bruneian"});
        COUNTRY_MAP.put("BG", new String[]{"Bulgaria", "Bulgarian"});
        COUNTRY_MAP.put("BF", new String[]{"Burkina Faso", "Burkinabe"});
        COUNTRY_MAP.put("BI", new String[]{"Burundi", "Burundian"});
        COUNTRY_MAP.put("CV", new String[]{"Cabo Verde", "Cape Verdean"});
        COUNTRY_MAP.put("KH", new String[]{"Cambodia", "Cambodian"});
        COUNTRY_MAP.put("CM", new String[]{"Cameroon", "Cameroonian"});
        COUNTRY_MAP.put("CA", new String[]{"Canada", "Canadian"});
        COUNTRY_MAP.put("CL", new String[]{"Chile", "Chilean"});
        COUNTRY_MAP.put("CN", new String[]{"China", "Chinese"});
        COUNTRY_MAP.put("CO", new String[]{"Colombia", "Colombian"});
        COUNTRY_MAP.put("CR", new String[]{"Costa Rica", "Costa Rican"});
        COUNTRY_MAP.put("HR", new String[]{"Croatia", "Croatian"});
        COUNTRY_MAP.put("CU", new String[]{"Cuba", "Cuban"});
        COUNTRY_MAP.put("CY", new String[]{"Cyprus", "Cypriot"});
        COUNTRY_MAP.put("CZ", new String[]{"Czech Republic", "Czech"});
        COUNTRY_MAP.put("DK", new String[]{"Denmark", "Danish"});
        COUNTRY_MAP.put("DM", new String[]{"Dominica", "Dominican"});
        COUNTRY_MAP.put("DO", new String[]{"Dominican Republic", "Dominican"});
        COUNTRY_MAP.put("EG", new String[]{"Egypt", "Egyptian"});
        COUNTRY_MAP.put("SV", new String[]{"El Salvador", "Salvadoran"});
        COUNTRY_MAP.put("EE", new String[]{"Estonia", "Estonian"});
        COUNTRY_MAP.put("ET", new String[]{"Ethiopia", "Ethiopian"});
        COUNTRY_MAP.put("FI", new String[]{"Finland", "Finnish"});
        COUNTRY_MAP.put("FR", new String[]{"France", "French"});
        COUNTRY_MAP.put("GM", new String[]{"Gambia", "Gambian"});
        COUNTRY_MAP.put("GE", new String[]{"Georgia", "Georgian"});
        COUNTRY_MAP.put("DE", new String[]{"Germany", "German"});
        COUNTRY_MAP.put("GH", new String[]{"Ghana", "Ghanaian"});
        COUNTRY_MAP.put("GR", new String[]{"Greece", "Greek"});
        COUNTRY_MAP.put("GT", new String[]{"Guatemala", "Guatemalan"});
        COUNTRY_MAP.put("GN", new String[]{"Guinea", "Guinean"});
        COUNTRY_MAP.put("HN", new String[]{"Honduras", "Honduran"});
        COUNTRY_MAP.put("HU", new String[]{"Hungary", "Hungarian"});
        COUNTRY_MAP.put("IS", new String[]{"Iceland", "Icelander"});
        COUNTRY_MAP.put("IN", new String[]{"India", "Indian"});
        COUNTRY_MAP.put("ID", new String[]{"Indonesia", "Indonesian"});
        COUNTRY_MAP.put("IR", new String[]{"Iran", "Iranian"});
        COUNTRY_MAP.put("IQ", new String[]{"Iraq", "Iraqi"});
        COUNTRY_MAP.put("IE", new String[]{"Ireland", "Irish"});
        COUNTRY_MAP.put("IL", new String[]{"Israel", "Israeli"});
        COUNTRY_MAP.put("IT", new String[]{"Italy", "Italian"});
        COUNTRY_MAP.put("JM", new String[]{"Jamaica", "Jamaican"});
        COUNTRY_MAP.put("JP", new String[]{"Japan", "Japanese"});
        COUNTRY_MAP.put("JO", new String[]{"Jordan", "Jordanian"});
        COUNTRY_MAP.put("KZ", new String[]{"Kazakhstan", "Kazakh"});
        COUNTRY_MAP.put("KE", new String[]{"Kenya", "Kenyan"});
        COUNTRY_MAP.put("KR", new String[]{"Korea", "Korean"});
        COUNTRY_MAP.put("XK", new String[]{"Kosovo", "Kosovar"});
        COUNTRY_MAP.put("LA", new String[]{"Laos", "Laotian"});
        COUNTRY_MAP.put("LB", new String[]{"Lebanon", "Lebanese"});
        COUNTRY_MAP.put("LI", new String[]{"Liechtenstein", "Liechtensteiner"});
        COUNTRY_MAP.put("LT", new String[]{"Lithuania", "Lithuanian"});
        COUNTRY_MAP.put("LU", new String[]{"Luxembourg", "Luxembourger"});
        COUNTRY_MAP.put("MY", new String[]{"Malaysia", "Malaysian"});
        COUNTRY_MAP.put("ML", new String[]{"Mali", "Malian"});
        COUNTRY_MAP.put("MT", new String[]{"Malta", "Maltese"});
        COUNTRY_MAP.put("MX", new String[]{"Mexico", "Mexican"});
        COUNTRY_MAP.put("MD", new String[]{"Moldova", "Moldovan"});
        COUNTRY_MAP.put("MN", new String[]{"Mongolia", "Mongolian"});
        COUNTRY_MAP.put("MA", new String[]{"Morocco", "Moroccan"});
        COUNTRY_MAP.put("NA", new String[]{"Namibia", "Namibian"});
        COUNTRY_MAP.put("NL", new String[]{"Netherlands", "Dutch"});
        COUNTRY_MAP.put("NI", new String[]{"Nicaragua", "Nicaraguan"});
        COUNTRY_MAP.put("NE", new String[]{"Niger", "Nigerien"});
        COUNTRY_MAP.put("NG", new String[]{"Nigeria", "Nigerian"});
        COUNTRY_MAP.put("NO", new String[]{"Norway", "Norwegian"});
        COUNTRY_MAP.put("PK", new String[]{"Pakistan", "Pakistani"});
        COUNTRY_MAP.put("PS", new String[]{"Palestine", "Palestinian"});
        COUNTRY_MAP.put("PA", new String[]{"Panama", "Panamanian"});
        COUNTRY_MAP.put("PY", new String[]{"Paraguay", "Paraguayan"});
        COUNTRY_MAP.put("PE", new String[]{"Peru", "Peruvian"});
        COUNTRY_MAP.put("PH", new String[]{"Philippines", "Filipino"});
        COUNTRY_MAP.put("PL", new String[]{"Poland", "Polish"});
        COUNTRY_MAP.put("PT", new String[]{"Portugal", "Portuguese"});
        COUNTRY_MAP.put("QA", new String[]{"Qatar", "Qatari"});
        COUNTRY_MAP.put("RO", new String[]{"Romania", "Romanian"});
        COUNTRY_MAP.put("RU", new String[]{"Russia", "Russian"});
        COUNTRY_MAP.put("SN", new String[]{"Senegal", "Senegalese"});
        COUNTRY_MAP.put("RS", new String[]{"Serbia", "Serbian"});
        COUNTRY_MAP.put("SG", new String[]{"Singapore", "Singaporean"});
        COUNTRY_MAP.put("SK", new String[]{"Slovakia", "Slovak"});
        COUNTRY_MAP.put("SI", new String[]{"Slovenia", "Slovenian"});
        COUNTRY_MAP.put("SO", new String[]{"Somalia", "Somali"});
        COUNTRY_MAP.put("ZA", new String[]{"South Africa", "South African"});
        COUNTRY_MAP.put("ES", new String[]{"Spain", "Spanish"});
        COUNTRY_MAP.put("SD", new String[]{"Sudan", "Sudanese"});
        COUNTRY_MAP.put("SE", new String[]{"Sweden", "Swedish"});
        COUNTRY_MAP.put("CH", new String[]{"Switzerland", "Swiss"});
        COUNTRY_MAP.put("SY", new String[]{"Syria", "Syrian"});
        COUNTRY_MAP.put("TW", new String[]{"Taiwan", "Taiwanese"});
        COUNTRY_MAP.put("TZ", new String[]{"Tanzania", "Tanzanian"});
        COUNTRY_MAP.put("TH", new String[]{"Thailand", "Thai"});       
        COUNTRY_MAP.put("TN", new String[]{"Tunisia", "Tunisian"});
        COUNTRY_MAP.put("TR", new String[]{"Turkey", "Turkish"});
        COUNTRY_MAP.put("UG", new String[]{"Uganda", "Ugandan"});
        COUNTRY_MAP.put("UA", new String[]{"Ukraine", "Ukrainian"});
        COUNTRY_MAP.put("AE", new String[]{"United Arab Emirates", "Emirati"});
        COUNTRY_MAP.put("GB", new String[]{"United Kingdom", "British"});
        COUNTRY_MAP.put("US", new String[]{"United States", "American"});
        COUNTRY_MAP.put("UY", new String[]{"Uruguay", "Uruguayan"});
        COUNTRY_MAP.put("VE", new String[]{"Venezuela", "Venezuelan"});
        COUNTRY_MAP.put("VN", new String[]{"Vietnam", "Vietnamese"});
        }

    
    public List<String> extractCountry(String phrase, List <String> nombres){
        List<String> countriesFound = new ArrayList<>();
        
        for (Map.Entry<String, String[]> entry : COUNTRY_MAP.entrySet()) {
            String[] countryInfo = entry.getValue();
            String country = countryInfo[0];
            String demonym = countryInfo[1];

            if (phrase.contains(country) || phrase.contains(demonym)) {
                countriesFound.add(entry.getKey());
                nombres.removeIf(nombre -> nombre.equalsIgnoreCase(country) || nombre.equalsIgnoreCase(demonym));
            }
        }
  
        return countriesFound;
    }
    
}
