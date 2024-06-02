package com.gfg.devdoc.Fragmentos_cliente

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gfg.devdoc.Administrador.ModeloCategoria
import com.gfg.devdoc.Cliente.AdaptadorCategoria_cliente
import com.gfg.devdoc.databinding.FragmentClienteDashboardBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception

class Fragment_cliente_dashboard : Fragment() {

private lateinit var binding :FragmentClienteDashboardBinding
    private lateinit var mContext: Context

    private lateinit var categoriasArraylist: ArrayList<ModeloCategoria>
    private lateinit var adaptadorCategoria : AdaptadorCategoria_cliente

    override fun onAttach(context: Context) {
        mContext= context
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentClienteDashboardBinding.inflate(LayoutInflater.from(context),container,false)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cargarCategorias()
        binding.BuscarCategoriaCliente.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(categoria: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    adaptadorCategoria.filter.filter((categoria))
                }catch (e: Exception){

                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun cargarCategorias() {
        categoriasArraylist= ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Categorias").orderByChild("categoria")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categoriasArraylist.clear()
                for (ds in snapshot.children){
                    val modelo =ds.getValue(ModeloCategoria::class.java)
                    categoriasArraylist.add(modelo!!)
                }
                adaptadorCategoria= AdaptadorCategoria_cliente(mContext,categoriasArraylist)
                binding.categoriaRv.adapter=adaptadorCategoria
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

}