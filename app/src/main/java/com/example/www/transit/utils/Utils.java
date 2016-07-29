package com.example.www.transit.utils;

import com.example.www.transit.R;
import com.example.www.transit.model.Location;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaskaran on 21/7/16.
 */
public class Utils {

    public static List<Location> decodePolyLines(String poly){
        int len = poly.length();
        int index = 0;
        List<Location> decoded = new ArrayList<Location>();
        int lat = 0;
        int lng = 0;

        while (index < len){
            int b;
            int shift = 0;
            int result = 0;
            do{
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            }while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >>1));
            lng += dlng;

            decoded.add(new Location(
                    lat / 100000d,
                    lng / 100000d
            ));
        }
        return decoded;
    }

    public static int toKms(String distance){
        int a = Integer.parseInt(distance);
        return a/1000;
    }

    public static int getIconResource(String mode){
        /*if (mode == "TRANSIT"){
            return R.drawable.ic_directions_transit_black_24dp;
        } else if (mode == "WALKING"){
            return R.drawable.ic_directions_walk_black_24dp;
        }
        //return -1;
        return R.drawable.ic_directions_walk_black_24dp;*/
        int image;
        switch (mode){
            case "TRANSIT":
                image = R.drawable.ic_directions_transit_black_24dp;
                break;
            case "WALKING":
                image = R.drawable.ic_directions_walk_black_24dp;
                break;
            default:
                image = -1;
        }
        return image;
    }

    public static String decodeInstructions(String instructions){
        String string = instructions;
        try {

            byte[] utf8 = string.getBytes("UTF-8");
            string = new String(utf8, "UTF-8");

        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return string;
    }

}
