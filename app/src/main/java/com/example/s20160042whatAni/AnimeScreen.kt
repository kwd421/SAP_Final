package com.example.s20160042whatAni

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage

enum class AnimeScreen {
    List,
    Detail
}

@Composable
fun AnimeApp(animeList: List<Anime>) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AnimeScreen.List.name,
    ) {
        composable(route = AnimeScreen.List.name) {
            AnimeList(navController, animeList)
        }
        composable(
            route = AnimeScreen.Detail.name + "/{index}",
            arguments = listOf(navArgument("index") {
                type = NavType.IntType
            })
        ) {
            val index = it.arguments?.getInt("index") ?: -1
            if(index >= 0)
                AnimeDetail(animeList[index])
        }
    }
}

@Composable
fun AnimeList(navController: NavController, list: List<Anime>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(list.size) {
            AnimeItem(navController, list, it)
        }
    }
}

@Composable
fun AnimeItem(navController: NavController,
              animeList: List<Anime>,
              index: Int) {
//    var expanded by remember { mutableStateOf(false) }

    Card(
//        modifier = Modifier.clickable { expanded = !expanded },
        modifier = Modifier.clickable {
            navController.navigate(AnimeScreen.Detail.name + "/$index")
        },
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
//                .background(Color(255, 210, 210))
                .padding(8.dp)
        ) {
            AsyncImage(
                model = animeList[index].poster,
                contentDescription = "애니 포스터 이미지",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .width(60.dp)
                    .height(100.dp)
                    .clip(RoundedCornerShape(percent = 10)),
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                TextTitle(animeList[index].title)
                TextMaker(animeList[index].maker)
            }
        }
//        AnimatedVisibility(visible = expanded) {
//            Column()
//            {
//                Text("감독: ${anime.director}")   // null 나오면 오류O
//                anime.story?.let { Text("줄거리: $it") }   // null 나와도 오류X
//            }
    }
}

@Composable
fun TextTitle(title: String) {
    Text(title, fontSize = 25.sp, lineHeight = 28.sp)
}

@Composable
fun TextMaker(maker: String) {
    Text(maker, fontSize = 20.sp)
}

@Composable
fun AnimeDetail(anime: Anime) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = CenterHorizontally
    ) {
        RatingBar(anime.rating)
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            anime.title,
            fontSize = 40.sp,
            textAlign = TextAlign.Center,
            lineHeight = 45.sp
        )
        Spacer(modifier = Modifier.height(16.dp))

        AsyncImage(
            model = anime.poster,
            contentDescription = "애니 포스터 이미지",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .width(400.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(anime.trailer)
            )
            startActivity(context, intent, null)
        }) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                YoutubeIcon()
                Spacer(modifier = Modifier.width(16.dp))
                Text("YouTube 예고편 감상", fontSize = 20.sp)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = anime.makerlogo,
                contentDescription = "제작사 로고 이미지",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(percent = 10))
            )
            Text(anime.maker,
                fontSize = 25.sp,
                textAlign = TextAlign.Center)
        }
        Spacer(modifier = Modifier.height(32.dp))

        anime.director?.let {
            Text(
                "감독\n$it",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                lineHeight = 25.sp
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        Text("줄거리", fontSize = (20.sp),
            modifier = Modifier.align(CenterHorizontally)
        )
        anime.story?.let {
            Text(
                it,
                fontSize = 18.sp,
                lineHeight = 23.sp
            )
        }

    }
}

@Composable
fun RatingBar(makers: Int) {
    Row {
        repeat(makers) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "makers",
                modifier = Modifier.size(48.dp),
                tint = Color.Red)
        }
    }
}

@Composable
fun YoutubeIcon() {
    Canvas(
        modifier = Modifier
            .size(70.dp)
    ) {

        val path = Path().apply {
            moveTo(size.width * .40f, size.height * .33f)
            lineTo(size.width * .69f, size.height * .50f)
            lineTo(size.width * .40f, size.height * .68f)
            close()
        }
        drawRoundRect(
            color = Color.Red,
            cornerRadius = CornerRadius(40f, 40f),
            size = Size(size.width, size.height * .70f),
            topLeft = Offset(size.width.times(.0f), size.height.times(.15f))
        )
        drawPath(color = Color.White, path = path)
    }
}