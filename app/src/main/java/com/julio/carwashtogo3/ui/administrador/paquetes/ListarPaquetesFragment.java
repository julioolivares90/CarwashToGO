package com.julio.carwashtogo3.ui.administrador.paquetes;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.navigation.Navigation;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.julio.carwashtogo3.R;
import com.julio.carwashtogo3.adapters.PaquetesRecyclerViewAdapater;
import com.julio.carwashtogo3.common.Constantes;
import com.julio.carwashtogo3.listeners.PaquetesOnItemClickListener;
import com.julio.carwashtogo3.model.Paquete;
import com.julio.carwashtogo3.ui.administrador.empresa.EditarEmpresaFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */


public class ListarPaquetesFragment extends Fragment {

    private boolean isTwoPane=false;
    private final List<Paquete> paquetesList = new ArrayList<> ();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference paquetesRef;

    public  ListarPaquetesFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){


        View view = inflater.inflate ( R.layout.fragment_listar_paquetes, container, false );
        paquetesRef = firebaseDatabase.getReference(Constantes.REF_PAQUETES);
        //if (view.findViewById ( R.id.detalle_empresa )!=null){
          //  isTwoPane = true;
        //}

         final RecyclerView recyclerView =view.findViewById(R.id.rb_paquetes_list);
         //getListaPaquete ();

 paquetesRef.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists ()){
                    paquetesList.clear ();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren ()){
                        Paquete paquete = snapshot.getValue(Paquete.class);
                        paquetesList.add(paquete);
                    }

                    recyclerView.setAdapter (new PaquetesRecyclerViewAdapater ( paquetesList, new PaquetesOnItemClickListener () {
                        @Override
                        public void OnClick(Paquete paquete) {
                            //aqui la logica  para ver el detalle

                            if (isTwoPane){
                                Bundle arguments = new Bundle();
                                arguments.putString(Constantes.UID_PAQUETE,paquete.getUid());
                                EditarPaqueteFragment editarPaqueteFragment = new EditarPaqueteFragment();
                                editarPaqueteFragment.setArguments(arguments);

                               /* queda pendiente crear el fragmente de diseño de datlle para poner este pedaso de codigo
                               getChildFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.detalle_paquete,editarPaqueteFragment)
                                        .commit();*/
                            }else {
                                Bundle argumets = new Bundle();
                                argumets.putString(Constantes.UID_PAQUETE,paquete.getUid());
                                EditarPaqueteFragment editarPaqueteFragment = new EditarPaqueteFragment();
                                editarPaqueteFragment.setArguments(argumets);
                                View view = getView();
                                assert view != null;
                                //Navigation.findNavController(view).navigate(R.id.action_listarPaqueteFragment_to_editarPaqueteFragment,argumets);
                            }



                        }
                    } ));
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText ( getContext (),"ocurrio un error"+databaseError.getMessage (),Toast.LENGTH_LONG ).show ();

            }
        } );


        return  view;
    }

    private  void getListaPaquete(){
        paquetesRef.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              if(dataSnapshot.exists ()){
                  for(DataSnapshot snapshot : dataSnapshot.getChildren ()){
                      Paquete paquete = snapshot.getValue (Paquete.class);
                      paquetesList.add ( paquete );
                  }
                    Log.d ( "paquete list", paquetesList.get (0).getTitulo ());
              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText (getContext (), "ocurrio un erros"+databaseError.getMessage (), Toast.LENGTH_LONG).show ();
            }
        } );
    }


}
