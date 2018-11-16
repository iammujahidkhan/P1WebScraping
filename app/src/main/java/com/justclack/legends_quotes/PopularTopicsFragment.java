package com.justclack.legends_quotes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import static com.justclack.legends_quotes.AllCatsModel.site;

/**
 * A simple {@link Fragment} subclass.
 */
public class PopularTopicsFragment extends Fragment {
    RecyclerView recyclerview;
    ArrayList<AllCatsModel> list = new ArrayList<>();
    String Url = site + "topics/";

    View mainView;

    public PopularTopicsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.activity_quotes_display, container, false);
        recyclerview = mainView.findViewById(R.id.recyclerview);
        recyclerview.hasFixedSize();
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        list.add(new AllCatsModel(Url, "age"));
        list.add(new AllCatsModel(Url, "alone"));
        list.add(new AllCatsModel(Url, "amazing"));
        list.add(new AllCatsModel(Url, "anger"));
        list.add(new AllCatsModel(Url, "anniversary"));
        list.add(new AllCatsModel(Url, "architecture"));
        list.add(new AllCatsModel(Url, "art"));
        list.add(new AllCatsModel(Url, "attitude"));
        list.add(new AllCatsModel(Url, "beauty"));
        list.add(new AllCatsModel(Url, "best"));
        list.add(new AllCatsModel(Url, "birthday"));
        list.add(new AllCatsModel(Url, "brainy"));
        list.add(new AllCatsModel(Url, "business"));
        list.add(new AllCatsModel(Url, "car"));
        list.add(new AllCatsModel(Url, "chance"));
        list.add(new AllCatsModel(Url, "change"));
        list.add(new AllCatsModel(Url, "christmas"));
        list.add(new AllCatsModel(Url, "communication"));
        list.add(new AllCatsModel(Url, "computers"));
        list.add(new AllCatsModel(Url, "cool"));
        list.add(new AllCatsModel(Url, "courage"));
        list.add(new AllCatsModel(Url, "dad"));
        list.add(new AllCatsModel(Url, "dating"));
        list.add(new AllCatsModel(Url, "death"));
        list.add(new AllCatsModel(Url, "design"));
        list.add(new AllCatsModel(Url, "diet"));
        list.add(new AllCatsModel(Url, "dreams"));
        list.add(new AllCatsModel(Url, "easter"));
        list.add(new AllCatsModel(Url, "education"));
        list.add(new AllCatsModel(Url, "environmental"));
        list.add(new AllCatsModel(Url, "equality"));
        list.add(new AllCatsModel(Url, "experience"));
        list.add(new AllCatsModel(Url, "failure"));
        list.add(new AllCatsModel(Url, "faith"));
        list.add(new AllCatsModel(Url, "family"));
        list.add(new AllCatsModel(Url, "famous"));
        list.add(new AllCatsModel(Url, "fathersday"));
        list.add(new AllCatsModel(Url, "fear"));
        list.add(new AllCatsModel(Url, "finance"));
        list.add(new AllCatsModel(Url, "fitness"));
        list.add(new AllCatsModel(Url, "food"));
        list.add(new AllCatsModel(Url, "forgiveness"));
        list.add(new AllCatsModel(Url, "freedom"));
        list.add(new AllCatsModel(Url, "friendship"));
        list.add(new AllCatsModel(Url, "funny"));
        list.add(new AllCatsModel(Url, "future"));
        list.add(new AllCatsModel(Url, "gardening"));
        list.add(new AllCatsModel(Url, "god"));
        list.add(new AllCatsModel(Url, "good"));
        list.add(new AllCatsModel(Url, "government"));
        list.add(new AllCatsModel(Url, "graduation"));
        list.add(new AllCatsModel(Url, "great"));
        list.add(new AllCatsModel(Url, "happiness"));
        list.add(new AllCatsModel(Url, "health"));
        list.add(new AllCatsModel(Url, "history"));
        list.add(new AllCatsModel(Url, "home"));
        list.add(new AllCatsModel(Url, "hope"));
        list.add(new AllCatsModel(Url, "humor"));
        list.add(new AllCatsModel(Url, "imagination"));
        list.add(new AllCatsModel(Url, "independence"));
        list.add(new AllCatsModel(Url, "inspirational"));
        list.add(new AllCatsModel(Url, "intelligence"));
        list.add(new AllCatsModel(Url, "jealousy"));
        list.add(new AllCatsModel(Url, "knowledge"));
        list.add(new AllCatsModel(Url, "leadership"));
        list.add(new AllCatsModel(Url, "learning"));
        list.add(new AllCatsModel(Url, "legal"));
        list.add(new AllCatsModel(Url, "life"));
        list.add(new AllCatsModel(Url, "love"));
        list.add(new AllCatsModel(Url, "marriage"));
        list.add(new AllCatsModel(Url, "medical"));
        list.add(new AllCatsModel(Url, "memorialday"));
        list.add(new AllCatsModel(Url, "men"));
        list.add(new AllCatsModel(Url, "mom"));
        list.add(new AllCatsModel(Url, "money"));
        list.add(new AllCatsModel(Url, "morning"));
        list.add(new AllCatsModel(Url, "mothersday"));
        list.add(new AllCatsModel(Url, "motivational"));
        list.add(new AllCatsModel(Url, "movies"));
        list.add(new AllCatsModel(Url, "movingon"));
        list.add(new AllCatsModel(Url, "music"));
        list.add(new AllCatsModel(Url, "nature"));
        list.add(new AllCatsModel(Url, "newyears"));
        list.add(new AllCatsModel(Url, "parenting"));
        list.add(new AllCatsModel(Url, "patience"));
        list.add(new AllCatsModel(Url, "patriotism"));
        list.add(new AllCatsModel(Url, "peace"));
        list.add(new AllCatsModel(Url, "pet"));
        list.add(new AllCatsModel(Url, "poetry"));
        list.add(new AllCatsModel(Url, "politics"));
        list.add(new AllCatsModel(Url, "positive"));
        list.add(new AllCatsModel(Url, "power"));
        list.add(new AllCatsModel(Url, "relationship"));
        list.add(new AllCatsModel(Url, "religion"));
        list.add(new AllCatsModel(Url, "respect"));
        list.add(new AllCatsModel(Url, "romantic"));
        list.add(new AllCatsModel(Url, "sad"));
        list.add(new AllCatsModel(Url, "saintpatricksday"));
        list.add(new AllCatsModel(Url, "science"));
        list.add(new AllCatsModel(Url, "smile"));
        list.add(new AllCatsModel(Url, "society"));
        list.add(new AllCatsModel(Url, "space"));
        list.add(new AllCatsModel(Url, "sports"));
        list.add(new AllCatsModel(Url, "strength"));
        list.add(new AllCatsModel(Url, "success"));
        list.add(new AllCatsModel(Url, "sympathy"));
        list.add(new AllCatsModel(Url, "teacher"));
        list.add(new AllCatsModel(Url, "technology"));
        list.add(new AllCatsModel(Url, "teen"));
        list.add(new AllCatsModel(Url, "thankful"));
        list.add(new AllCatsModel(Url, "thanksgiving"));
        list.add(new AllCatsModel(Url, "time"));
        list.add(new AllCatsModel(Url, "travel"));
        list.add(new AllCatsModel(Url, "trust"));
        list.add(new AllCatsModel(Url, "truth"));
        list.add(new AllCatsModel(Url, "valentinesday"));
        list.add(new AllCatsModel(Url, "veteransday"));
        list.add(new AllCatsModel(Url, "war"));
        list.add(new AllCatsModel(Url, "wedding"));
        list.add(new AllCatsModel(Url, "wisdom"));
        list.add(new AllCatsModel(Url, "women"));
        list.add(new AllCatsModel(Url, "work"));
        recyclerview.setAdapter(new Cats_Main_Adapter(list, getActivity()));
        return mainView;
    }
}