package com.example.djqrj.allah;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ekalips on 3/8/16.
 */
public class EffectCollection {

    private ArrayList<Effect> Collection = new ArrayList<>();
    public EffectCollection()
    {
        Collection.add(new Effect(0,"None"));
        Collection.add(new Effect(1,"Autofix"));
        Collection.add(new Effect(2,"Min/Max Color Intensity"));
        Collection.add(new Effect(3,"Brightness"));
        Collection.add(new Effect(4,"Contrast"));
        Collection.add(new Effect(5,"Cross Process"));
        Collection.add(new Effect(6,"Documentary"));
        Collection.add(new Effect(7,"Duo Tone"));
        Collection.add(new Effect(8,"Fill Light"));
        Collection.add(new Effect(9,"Fish Eye"));
        Collection.add(new Effect(10,"Flip Vertical"));
        Collection.add(new Effect(11,"Flip Horizontal"));
        Collection.add(new Effect(12,"Grain"));
        Collection.add(new Effect(13,"Grayscale"));
        Collection.add(new Effect(14,"Lomoish"));
        Collection.add(new Effect(15,"Negative"));
        Collection.add(new Effect(16,"Posterize"));
        Collection.add(new Effect(17,"Rotate"));
        Collection.add(new Effect(18,"Saturate"));
        Collection.add(new Effect(19,"Sepia"));
        Collection.add(new Effect(20,"Sharpen"));
        Collection.add(new Effect(21,"Temperature"));
        Collection.add(new Effect(22,"Tint"));
        Collection.add(new Effect(23,"Vignette"));
    }

    public Effect[] toArray()
    {
        Effect[] asd = new Effect[Collection.size()];
        for (int i=0;i<Collection.size();i++)
        {
            asd[i] = Collection.get(i);
        }
        return asd;
    }
    public Effect[] getArray(){return toArray();}
    public Effect getEffect(int i) {return Collection.get(i);}
    public class Effect {
        int id;
        String name;

        public Effect(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
