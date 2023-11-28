package com.example.doscaras

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), Adaptador.OnItemClickListener {

    lateinit var caraAbajo: List<Item>
    private val imagenes = Imagenes()
    private var ultimaCartaVolteada: Item? = null
    private lateinit var adaptador: Adaptador
    lateinit var prueba: List<Item>
    var listselected = imagenes.pokemon
    var vidas = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        // Manejo del NavigationView
        findViewById<NavigationView>(R.id.nav_view).setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navZelda -> {
                    Toast.makeText(this, "Cartas de Zelda", Toast.LENGTH_SHORT).show()
                    adaptador.resetCards()
                    listselected = imagenes.zelda
                    onStartGame(imagenes.zelda)
                    findViewById<DrawerLayout>(R.id.drawer).closeDrawer(GravityCompat.START)
                    true
                }

                R.id.navArmas -> {
                    Toast.makeText(this, "Cartas de Armas", Toast.LENGTH_SHORT).show()
                    adaptador.resetCards()
                    listselected = imagenes.weapons
                    onStartGame(imagenes.weapons)
                    findViewById<DrawerLayout>(R.id.drawer).closeDrawer(GravityCompat.START)
                    true
                }

                R.id.navPokemon -> {
                    Toast.makeText(this, "Cartas de Pokemon", Toast.LENGTH_SHORT).show()
                    adaptador.resetCards()
                    listselected = imagenes.pokemon
                    onStartGame(imagenes.pokemon)
                    findViewById<DrawerLayout>(R.id.drawer).closeDrawer(GravityCompat.START)
                    true
                }

                R.id.navReiniciar -> {
                    Toast.makeText(this, "Reiniciando", Toast.LENGTH_SHORT).show()
                    adaptador.resetCards()
                    listselected.shuffled()
                    onStartGame(listselected)
                    findViewById<DrawerLayout>(R.id.drawer).closeDrawer(GravityCompat.START)
                    true
                }

                else -> false
            }
        }

        onStartGame(imagenes.pokemon)
    }

    private fun onStartGame(selectedList: List<Item>) {
        findViewById<TextView>(R.id.txtVidas).text = "Vidas: $vidas"
        ultimaCartaVolteada = null
        caraAbajo = imagenes.abajo
        prueba = selectedList.shuffled()

        val rv = findViewById<RecyclerView>(R.id.contenedor)
        adaptador = Adaptador(caraAbajo, this)
        rv.adapter = adaptador
        rv.layoutManager = GridLayoutManager(this, 4)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btAbrirnav -> {
                findViewById<DrawerLayout>(R.id.drawer).openDrawer(GravityCompat.START)
                true
            }

            R.id.btCerrarnav -> {
                finish()
                true
            }

            R.id.holaPedro -> {
                Toast.makeText(this, "Hola Pedro!!", Toast.LENGTH_SHORT).show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onItemClick(position: Int) {
        val rv = findViewById<RecyclerView>(R.id.contenedor)

        if (position < 0 || position >= caraAbajo.size) {
            // Verificar si la posición es válida
            return
        }

        val clickedCard = caraAbajo[position]

        // Verificar si la carta ya está girada o resuelta
        if (!clickedCard.girada && !clickedCard.resuelta) {
            clickedCard.girada = true
            clickedCard.imgid = prueba[position].imgid
            rv.adapter?.notifyItemChanged(position)

            if (ultimaCartaVolteada != null) {
                if (clickedCard.imgid == ultimaCartaVolteada!!.imgid) {
                    clickedCard.resuelta = true
                    ultimaCartaVolteada!!.resuelta = true

                    if (caraAbajo.all { it.resuelta }) {
                        Toast.makeText(this, "¡Juego completado!", Toast.LENGTH_SHORT).show()
                    }
                    ultimaCartaVolteada = null

                } else {
                    Thread {
                        Thread.sleep(1000)

                        runOnUiThread {
                            vidas--
                            findViewById<TextView>(R.id.txtVidas).text = "Vidas: $vidas"
                            if (vidas == 0){
                                Toast.makeText(this, "¡Vidas acabadas!", Toast.LENGTH_SHORT).show()
                                adaptador.resetCards()
                                listselected.shuffled()
                                vidas = 5
                                findViewById<TextView>(R.id.txtVidas).text = "Vidas: $vidas"
                            }
                            clickedCard.girada = false
                            ultimaCartaVolteada!!.girada = false
                            rv.adapter?.notifyItemChanged(position)
                            val lastPosition = caraAbajo.indexOf(ultimaCartaVolteada!!)
                            if (lastPosition != -1) {
                                rv.adapter?.notifyItemChanged(lastPosition)
                            }
                            ultimaCartaVolteada = null
                        }
                    }.start()
                }
            } else {
                ultimaCartaVolteada = clickedCard
            }
        }
    }
}
