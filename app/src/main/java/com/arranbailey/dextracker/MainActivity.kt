package com.arranbailey.dextracker

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewModelScope
import com.arranbailey.dextracker.ui.theme.DextrackerTheme
import coil.compose.AsyncImage
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DextrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CardSearchScreen()
                }
            }
        }
    }
}

data class CardResponse(
    val data: List<Card>
)

data class Card(
    val id: String,
    val name: String,
    val images: CardImages,
    val rarity: String?,
    val set: CardSet?
)

data class CardImages(
    val small: String,
    val large: String
)

data class CardSet(
    val name: String,
    val series: String
)


interface PokeApiService {
    @GET("cards")
    suspend fun searchCards(
        @Query("q") query: String,
        @Query("pageSize") pageSize: Int = 50
    ): CardResponse
}

object RetrofitInstance {
    private const val BASE_URL = "https://api.pokemontcg.io/v2/"
    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    val api: PokeApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(PokeApiService::class.java)
    }
}

class CardViewModel : ViewModel() {
    var cards = mutableStateOf<List<Card>>(emptyList())
        private set

    var isLoading = mutableStateOf(false)
        private set

    fun search(query: String) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = RetrofitInstance.api.searchCards("name:$query")
                Log.d("DEBUG", "Raw response: ${response.data.size}")
                cards.value = response.data
                val rawJson = Gson().toJson(response)
                Log.d("RAW_JSON", rawJson)
            } catch (e: Exception) {
                // Handle error
                cards.value = emptyList()
            }
            isLoading.value = false
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardSearchScreen(viewModel: CardViewModel = viewModel()) {
    var searchQuery by remember { mutableStateOf("") }
    Log.d("Info", searchQuery)
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search Pokémon") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { viewModel.search(searchQuery) },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Search")
        }

        if (viewModel.isLoading.value) {
            CircularProgressIndicator()
        } else {
            LazyColumn {
                items(viewModel.cards.value) { card ->
                    CardItem(card)
                }
            }
        }
    }
}

@Composable
fun CardItem(card: Card) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)) {

        AsyncImage(
            model = card.images.small,
            contentDescription = card.name,
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(card.name, fontWeight = FontWeight.Bold)
            Text(card.rarity ?: "Unknown rarity", fontSize = 12.sp)
            Text(card.set?.name ?: "Unknown set", fontSize = 12.sp)
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column (modifier = modifier
        .background(color = Color.Black)

    ){
        Text(
            text = "Hello $name!",
            color = Color.Green,
            fontStyle = FontStyle.Italic,
            modifier = modifier
                .background(color = Color.Black)
        )
        Text(text = "Hello")

    }
}

@Composable
fun CardImagePreview() {
    val imageUrl = "https://images.pokemontcg.io/xy1/1_hires.png"

    AsyncImage(
        model = imageUrl,
        contentDescription = "Venusaur EX card",
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(12.dp))
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DextrackerTheme {
        CardSearchScreen()
    }
}