package com.quadrant.govolt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.quadrant.adapters.ExpandableListAdapterLat;
import com.quadrant.adapters.MyExpandableListAdapter;
import com.quadrant.adapters.MyExpandableListAdapterLatest;
import com.quadrant.govolt.Others.Constants;
import com.quadrant.govolt.Others.PreferenceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class FAQ_Activity extends AppCompatActivity {

    private ImageView back_img;

    ExpandableListView expandableListView;

    // Create ExpandableListAdapterLat
    ExpandableListAdapterLat expandableListAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    private CircleImageView _profileImg;


         private ImageView Home_icon;




    List<String> parents; // A list of parents (strings)
    Map<String, List<String>> childrenMap; // An object that maps keys to values.
    // A map cannot contain duplicate keys; each key can map to at most one value
    // Map Documentation: https://developer.android.com/reference/java/util/Map.html
    // HashMap Documentation: https://developer.android.com/reference/java/util/HashMap.html
    // Hashtable Documentation: https://developer.android.com/reference/java/util/Hashtable.html

    private String TAG = "FAQ_Activity";


    Map<String, String> childrenMapLatest;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_faq);
        context = this;

        _profileImg=(CircleImageView)findViewById(R.id.profile_image) ;


        Home_icon=(ImageView) findViewById(R.id.home_icon) ;

        expandableListView = (ExpandableListView) findViewById(R.id.list_view_expandable);

        TextView title = (TextView)findViewById(R.id.title);
        title.setText("FAQ");


        String _avathar_loc_img = PreferenceUtil.getInstance().getString(context, Constants.AVATHAR_LOC_IMG, "");
        Glide.with(context)
                .load(_avathar_loc_img) // or URI/path
                .placeholder(R.drawable.ic_navigation_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.IMMEDIATE)
                .error(R.drawable.ic_navigation_icon)
                .skipMemoryCache(false)
                .into(_profileImg);

        ImageView back  = (ImageView)findViewById(R.id.back_img);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

       loadTestFile();

        Home_iconClick();
       // fillData();

/*
        Log.e(TAG, "size 1---" + parents.size());
        Log.e(TAG, "size 2---" + childrenMap.size());*/

       // expandableListView = (ExpandableListView) findViewById(R.id.list_view_expandable);
        back_img = (ImageView) findViewById(R.id.back_img);



        /** Set a listener on the child elements - show toast as an example */
        /*expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(context, parents.get(groupPosition) + " : " + childrenMap.get(parents.get(groupPosition)).get(childPosition), Toast.LENGTH_LONG).show();
                return false;

            }
        });*/

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




    }

    private void Home_iconClick() {


        Home_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(FAQ_Activity.this,HomeActivity.class);
                startActivity(I);
            }
        });


    }

    private JSONObject createJSONFromFile(int fileID) {

        JSONObject result = null;

        try {
            // Read file into string builder
            InputStream inputStream = context.getResources().openRawResource(fileID);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();

            for (String line = null; (line = reader.readLine()) != null ; ) {
                builder.append(line).append("\n");
            }

            // Parse into JSONObject
            String resultStr = builder.toString();
            JSONTokener tokener = new JSONTokener(resultStr);
            result = new JSONObject(tokener);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }

    private void loadTestFile() {
      try {
            // List of Parent Items
            childrenMap = new HashMap<>();
            List<String> children = new ArrayList<>();
            ArrayList<String>  _parents = new ArrayList<>();


            JSONObject data = createJSONFromFile(R.raw.helpfaq);
            // ... do awesome stuff with test data ...
             Log.e(TAG, "DATA---"+data.toString());
            JSONArray data_array = data.getJSONArray("helpfaq");
          listDataHeader = new ArrayList<String>();
          listDataChild = new HashMap<String, List<String>>();
            for(int i=0; i<data_array.length(); i++){
                JSONObject obj = data_array.getJSONObject(i);

                listDataHeader.add(obj.getString("title"));

                List<String> lease_offer = new ArrayList<String>();

                lease_offer.add(obj.getString("description"));

              /* *//* String _title = obj.getString("title");
                String _description = obj.getString("description");*//*

                Log.e(TAG, "title---"+_title);


                _parents.add(""+_title);
                children.add(""+_description);
*/
                listDataChild.put(listDataHeader.get(i), lease_offer);
             //   childrenMapLatest.put(_parents.get(i), children.get(i));
            }

          expandableListAdapter = new ExpandableListAdapterLat(getApplicationContext(), listDataHeader, listDataChild);

          // Set the value in the ExpandableListView
          expandableListView.setAdapter(expandableListAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void fillData() {

        parents = new ArrayList<>(); // List of Parent Items
        childrenMap = new HashMap<>(); // HashMap to map keys to values

        // Add parents to the parents list
        parents.add("Cos’è GoVolt?");
        parents.add("Cosa devo sapere prima di\n" +
                "guidare uno scooter elettrico?");
        parents.add("Dove posso trovare lo scooter GoVolt?");
        parents.add("Quanto dura la batteria?");


        parents.add("Qual è l’età minima richiesta per guidare GoVolt?");
        parents.add("Ho bisogno di una patente speciale per guidare GoVolt?");
        parents.add("Ho bisogno di un’assicurazione per guidare?");
        parents.add("Dove trovo i documenti relativi allo scooter?");
        parents.add("Devo avere un casco?");
        parents.add("Come funziona l'App una volta registrati?");
        parents.add("Come noleggio uno scooter GoVolt?");
        parents.add("Dove si trova la chiave?");
        parents.add("Sono in sella allo scooter e non funziona. Cosa faccio?");
        parents.add("Lo scooter è danneggiato, cosa faccio?");

        parents.add("Dove posso guidare lo scooter GoVolt?");
       /* parents.add("Posso utilizzare il mio scooter fuori dall’area autorizzata?");

        parents.add(" Esiste un limite massimo o minimo di minuti o chilometri durante la stessa corsa?");*/
        // Create lists to hold the data for the children of the parents
        List<String> susanChildren = new ArrayList<>();
        List<String> jeffChildren = new ArrayList<>();
        List<String> threechild = new ArrayList<>();
        List<String> fourchild=new ArrayList<>();
        List<String> fivechild=new ArrayList<>();
        List<String> sixchild=new ArrayList<>();
        List<String> sevenchild=new ArrayList<>();
        List<String> eightchild=new ArrayList<>();
        List<String> ninechild=new ArrayList<>();
        List<String> tenchild=new ArrayList<>();
        List<String> elevenchild=new ArrayList<>();
        List<String> twelechild=new ArrayList<>();
        List<String> thirteenchild=new ArrayList<>();
        List<String> fourteenchild=new ArrayList<>();

        List<String> fifteenchild=new ArrayList<>();



/*

        List<String> susanChildren1 = new ArrayList<>();
        List<String> jeffChildren1 = new ArrayList<>();

        List<String> susanChildren2 = new ArrayList<>();
        List<String> jeffChildren2 = new ArrayList<>();

        List<String> susanChildren3 = new ArrayList<>();
        List<String> jeffChildren3 = new ArrayList<>();

        List<String> susanChildren4 = new ArrayList<>();
        List<String> jeffChildren4 = new ArrayList<>();
*/

        // Add children to the children lists
        susanChildren.add("GoVolt è un servizio di scooter-sharing elettrico ‘free-floating’ attivo nella città di Milano, Italia. Consiste nel noleggiare uno scooter tramite un’APP per il tempo necessario, avendo a disposizione un mezzo di trasporto urbano che è semplice, facile, comodo, sostenibile e silenzioso. Con GoVolt, puoi guidare scooter elettrici nella tua città dove e come vuoi!");
        /*susanChildren.add("Child - Guy");
        susanChildren.add("Child - Mark");*/

        jeffChildren.add("Gli scooter elettrici sono molto silenziosi: pedoni, animali o altre persone e veicoli nelle vicinanze potrebbero non sentirti passare," +
                "quindi assicurati che ti vedano e non esitare ad usare il clacson così" +
                "da evitare situazioni pericolose. Gli scooter sono inoltre molto reattivi" +
                "non appena si accelera: soprattutto in fase di partenza (0 km/h)." +
                "Durante l’accelerazione, fate attenzione a non essere sorpresi dalla" +
                "potenza e del motore elettrico.");



        threechild.add("GoVolt è a Milano, Italia. Tramite l’App puoi noleggiare qualsiasi scooter presente all’interno dell’area operativa, evidenziata in verde sulla mappa dell’App.");
        fourchild.add("La batteria ha una capacità di 100 km. Non preoccuparti, faremo sempre in modo che tu abbia batteria sufficiente per la tua corsa. Qualsiasi scooter con un livello di batteria basso non sarà prenotabile e la cambieremo appena possibile.");

        fivechild.add("Nonostante gli scooter siano molto semplici da guidare, l’età minima richiesta è di 18 anni per le condizioni assicurative.");
        sixchild.add("Non c’è bisogno di alcuna patente speciale per guidare uno scooter GoVolt. Basta avere una patente AM, A1 o B. Accettiamo anche patenti internazionali, se autorizzate in Italia.");
        sevenchild.add("L’assicurazione è inclusa nel prezzo di ogni corsa ed è associata ad ogni utilizzatore. Grazie alla nostra assicurazione Kasko, proteggiamo te, lo scooter e i terzi.");
        eightchild.add("Se usi gli scooter GoVolt devi sempre indossare il casco!\n" +
                "Non è necessario portare il tuo casco. Ogni scooter ha nel proprio bauletto due caschi di misure diverse: una M ed una XL (potrai facilmente controllare la taglia sul retro del casco). È inoltre possibile utilizzare una cuffietta igienica, anche questa disponibile all’interno del bauletto. Se volessi utilizzare il tuo casco, è necessario che sia certificato e in buone condizioni.\n");
        ninechild.add("Devi semplicemente aprire l’App, cercare lo scooter più vicino a te, prenotarlo, quindi hai 15 minuti di tempo per raggiungere il posto in cui si trova lo scooter e sbloccarlo. Lo scooter si accenderà e il bauletto si aprirà automaticamente. Metti il casco, siediti sullo scooter, schiaccia due volte il pulsante rosso situato sulla destra del manubrio, togli il cavalletto e sei pronto per guidare in giro per Milano!");
        tenchild.add("GoVolt è completamente senza chiave! Non hai bisogno di una chiave né per accendere lo scooter, né per aprire il bauletto.");
        elevenchild.add("Assicurati di aver premuto per due volte il bottone rosso sul lato destro del manubrio. Se lo scooter non parte comunque, prova a prenotarne un altro vicino a te. Puoi riportare ogni problema a: servizioclienti@govoltmobility.com");
        twelechild.add("Se lo scooter è danneggiato o se manca il casco per favore fotografa il danno e mandaci le foto a servizioclienti@govoltmobility.com\n" +
                "Ce ne prenderemo cura immediatamente!\n");
        thirteenchild.add("Puoi guidare GoVolt ovunque tu voglia! Ovviamente, strade ad alta velocità, come le autostrade, sono inaccessibili per gli scooter. Ricordati solo che per terminare la corsa dovrai parcheggiare all’interno dell’area operativa evidenziata in verde sulla mappa dell’App.\n" +
                "\n" +
                "Durante la corsa\n");
        fourteenchild.add("Certamente, con gli Scooter GoVolt, puoi lasciare la business area. Il noleggio deve però terminare entro l’area consentita.");
        fifteenchild.add("Non c’è un limite, paghi in base al tempo di utilizzo, tutto qui!");


        childrenMap.put(parents.get(0), susanChildren);

        childrenMap.put(parents.get(1), jeffChildren);
        childrenMap.put(parents.get(2), threechild);
        childrenMap.put(parents.get(3), fourchild);

        childrenMap.put(parents.get(4), fivechild);

        childrenMap.put(parents.get(5), sixchild);

        childrenMap.put(parents.get(6), sevenchild);

        childrenMap.put(parents.get(7), eightchild);

        childrenMap.put(parents.get(8), ninechild);

        childrenMap.put(parents.get(9), tenchild);
        childrenMap.put(parents.get(10), elevenchild);

        childrenMap.put(parents.get(11), twelechild);
        childrenMap.put(parents.get(12), thirteenchild);
        childrenMap.put(parents.get(13), fourteenchild);
        childrenMap.put(parents.get(14), fifteenchild);







/*
        susanChildren2.add("");
        jeffChildren2.add("");


        childrenMap.put(parents.get(2), susanChildren1);
        childrenMap.put(parents.get(3), jeffChildren1);

        childrenMap.put(parents.get(4), susanChildren2);
        childrenMap.put(parents.get(5), jeffChildren2);*/

    }


}
