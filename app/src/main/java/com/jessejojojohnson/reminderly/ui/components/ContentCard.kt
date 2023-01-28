package com.jessejojojohnson.reminderly.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jessejojojohnson.reminderly.R
import com.jessejojojohnson.reminderly.models.ContentItem
import com.jessejojojohnson.reminderly.models.ContentSource
import com.jessejojojohnson.reminderly.ui.theme.ReminderlyTheme
import java.util.*

@Composable
fun ContentCard(item: ContentItem) {
    ElevatedCard(
        shape = RoundedCornerShape(3.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        HeaderImage(item = item)
        Body(item = item)
        Footer(item = item)
    }
}

@Composable
fun HeaderImage(item: ContentItem) {
    AsyncImage(
        model = item.imageUrl,
        placeholder = painterResource(id = R.drawable.ic_placeholder),
        contentDescription = "Content image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .background(Color.Gray)
    )
}

@Composable
fun Body(item: ContentItem) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(
            text = item.title,
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = item.text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun Footer(item: ContentItem) {
    Row (modifier = Modifier.padding(8.dp)) {
        Icon(
            painter = painterResource(id = item.source.iconResource),
            contentDescription = "${item.source.name} logo",
            tint = Color.Gray,
            modifier = Modifier.size(7.dp)
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = "from ${item.source.name}, saved on ${longToDateString(item.dateSaved)}",
            fontSize = TextUnit(5F, TextUnitType.Sp),
            color = Color.Gray,
            modifier = Modifier.wrapContentWidth()
        )
    }
}

fun longToDateString(long: Long): String {
    return Date(long).toString()
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ReminderlyTheme {
        ContentCard(
            ContentItem(
                imageUrl = "",
                title = "My First Reminder!",
                text = "Hello, world! Let's pretend this is content I saved on the Interwebs!",
                dateSaved = System.currentTimeMillis(),
                source = ContentSource.HackerNews
            )
        )
    }
}