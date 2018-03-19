package com.confortapp.leon.confortapp.ExpandableList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leon on 19.03.2018.
 */

public class InfoData {
    private List<Info> infoList = new ArrayList<Info>() {
        {
            add(new Info(0, "Buiucani:\n" +
                    "MD 2069, Chișinau, str.Mesager, 19\n" +
                    "Anticamera: (+373) 22 74-18-36; Fax:(+373) 22 74-76-14;\n" +
                    "Secția realizare: (+373) 22 74-17-08; Salon-magazin: (+373) 22 84-30-61\n" +
                    "\n" +
                    "Ciocana:\n" +
                    "Str. Mircea cel Bătrân 2,\n" +
                    "Centrul comercial, etajul trei\n" +
                    "Orele de lucru: luni-duminică 09:00-19:00\n" +
                    "Telefon: (+373) 60055399", "Chisinau"));

            add(new Info(1, "\"Ilie Gargalic\" II\n" +
                    "Mag. Ilie, str. Gării 79 (Vokzalinaia),\n" +
                    "Tel. 0297-256-61 Mag. Ilie, str. Karl Marks 61,\n" +
                    "Mob: 069-736-737", "AneniiNoi"));;

            add(new Info(2, "\"Bozon\" SA, mag. \"MOBILĂ\"\n" +
                    "str. Ştefan cel Mare 2/1 regiunea Autogării,\n" +
                    "Mob. 069-555-229\n" +
                    "\n" +
                    "\"Imex Mobile\" SRL\n" +
                    "str. Piaţa Cara Ciobanu ( Boidiucova ), mag.989 D,\n" +
                    "Mob: 068-999-258\n" +
                    "str. Nicolae Iorga 8, Magazin de Mobilă (vis-a-vis METRO)\n" +
                    "Mob: 068-999-258\n" +
                    "\n" +
                    "\"Vlad\" SRL, mag. \"MOBILĂ\"\n" +
                    "str. Ştefan cel Mare 2/1, regiunea Autogării\n" +
                    "Mob: 069-388-049\n" +
                    "\n" +
                    "\"Mistel Mobil\" SRL mag. \"MOBILĂ\",\n" +
                    "str. Ştefan cel Mare 2/1, regiunea Autogării\n" +
                    "Tel.0231-44-733\n" +
                    "\n" +
                    "\"Covalciuc Sergiu\" ÎI\n" +
                    "str. Nicolae Iorga 16/1, mag. \"Interier\"\n" +
                    "Tel: 0231-477-90\n" +
                    "\n" +
                    "\"Surdu Iurie\"\n" +
                    "str. Feroviară 4, Piaţa materiale de Constructii, but.11\n" +
                    "Mob: 068-312-245\n" +
                    "\n" +
                    "\"Sebacom Plus\" SRL\n" +
                    "str. Piaţa Cara Ciobanu (Boidiucova),\n" +
                    "tel.0692-60-350", "Balti"));

            add(new Info(3, "\"Ilie Gargalic\" II\n" +
                    "Mag. Ilie, str. Gării 79 (Vokzalinaia),\n" +
                    "Tel. 0297-256-61 Mag. Ilie, str. Karl Marks 61,\n" +
                    "Mob: 069-736-737", "Basarabeasca"));
        }
    };

    private List<String> infoTypeList = new ArrayList<String>() {
        {
            add("Chisinau");
            add("AneniiNoi");
            add("Balti");
            add("Basarabeasca");
        }
    };

    public List<Info> getInfoList() {
        return infoList;
    }

    public List<String> getInfoTypeList() {
        return infoTypeList;
    }
}
