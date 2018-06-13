package pl.agh.dynamicsearch.models;

import android.content.Context;

import java.util.ArrayList;

import pl.agh.dynamicsearch.R;

public class Contains {

    private ArrayList<String> allContains = new ArrayList<>();
    private ArrayList<String> containsGroup = new ArrayList<>();
    private ArrayList<String> types = new ArrayList<>();
    private Context context;

    public Contains(Context context){
        this.context = context;
    }

    public ArrayList<String> getContainsGroup(){
        containsGroup.add(context.getString(R.string.substancje_slodzace));
        containsGroup.add(context.getString(R.string.barwniki));
        containsGroup.add(context.getString(R.string.konserwanty));
        containsGroup.add(context.getString(R.string.przeciwutleniacze));
        containsGroup.add(context.getString(R.string.regulatory_kwasowosci));
        containsGroup.add(context.getString(R.string.emulgatory));

        return containsGroup;
    }

    public ArrayList<String> getProductTypes() {
        types.add(context.getString(R.string.nabial));
        types.add(context.getString(R.string.słodycze));
        types.add(context.getString(R.string.pieczywo));
        types.add(context.getString(R.string.przemyslowe));
        types.add(context.getString(R.string.elektronika));
        types.add(context.getString(R.string.napoje));
        types.add(context.getString(R.string.alkochole));
        types.add(context.getString(R.string.jedzenie));
        types.add(context.getString(R.string.wedliny));
        return types;
    }

    public ArrayList<String> getContains(int position) {
        switch (position) {
            case 0:
                allContains.add("E 234 - nizyna");
                allContains.add("E 284 - kwas borowy");
                allContains.add("E 280 - kwas propionowy");
                allContains.add("E 242 - dimetylodiwęglan");
                allContains.add("E 249 - azotan (III) potasu");
                allContains.add("E 252 - azotan (V) potasu");
                break;
            case 1:
                allContains.add("E310-E312 galusany");
                allContains.add("E320 BHA");
                allContains.add("E321 BHT");
                break;

            case 2:
                for (int i = 200; i < 300; i++) {
                    allContains.add("E" + i);
                }
                break;

            case 3:
                allContains.add("E270 – kwas mlekowy (regulator kwasowości)");
                allContains.add("E330 – kwas cytrynowy");
                allContains.add("E300 – kwas askorbinowy ");
                allContains.add("E301 – askorbinian sodu");
                allContains.add("E302 – askorbinian wapnia");
                allContains.add("E304 – estry kwasów tłuszczowych i kwasu askorbinowego");
                allContains.add("E306 – mieszanina tokoferoli");

                break;

            case 4:
                allContains.add("E260 - kwas octowy");
                allContains.add("E330 - kwas cytrynowy");
                allContains.add("E270 - kwas mlekowy");
                allContains.add("E296 - kwas jabłkowy");
                allContains.add("E297 - kwas fumarowy");
                break;

            case 5:
                allContains.add("E 400 kwas alginowy");
                allContains.add("E 401 alginian sodu");
                allContains.add("E 402 alginian potasu");
                allContains.add("E 404 alginian wapnia");
                allContains.add("E 405 alginian glikolu propylenowego");
                allContains.add("E 407 karagen");
                allContains.add("E 410 mączka chleba świętojańskiego, guma karobowa");
                allContains.add("E 412 guma guar");
                allContains.add("E 414 guma arabska");
                allContains.add("E 415 guma kasantowa");
                allContains.add("E 420 sorbitol");
                allContains.add("E 422 glicerol");
                allContains.add("E 440 pektyny");
                allContains.add("E 450 fosforany");
                allContains.add("E 460 celuloza");
                allContains.add("E 461 metyloceluloza");
                allContains.add("E 463 hydroksypropyloceluloza");
                allContains.add("E 465 etylometyloceluloza");
                allContains.add("E 466 karboksymetyloceluloza");
                allContains.add("E 471a – E 471f mono- i diglicerydy kwasów tłuszczowych");
                allContains.add("E 477 estry kwasów tłuszczowych i glikolu propylenowego");
                break;
        }
        return allContains;
    }

}
