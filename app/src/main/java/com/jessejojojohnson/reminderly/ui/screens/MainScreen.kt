package com.jessejojojohnson.reminderly.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jessejojojohnson.reminderly.data.AppDatabase
import com.jessejojojohnson.reminderly.data.ContentDao
import com.jessejojojohnson.reminderly.data.Source
import com.jessejojojohnson.reminderly.ui.models.ContentItem
import com.jessejojojohnson.reminderly.ui.models.ContentSource
import com.jessejojojohnson.reminderly.ui.components.ContentCard
import kotlinx.coroutines.flow.map
import org.koin.java.KoinJavaComponent.inject

@Composable
fun MainScreen(
    modifier: Modifier,
    viewModel: MainScreenViewModel = viewModel(factory = MainScreenViewModel.Factory)
){
    val contentList by viewModel.getContent().collectAsState(initial = emptyList())
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(contentList) { item: ContentItem ->
            ContentCard(item = item)
        }
    }
}

class MainScreenViewModel(private val contentRepository: ContentRepository) : ViewModel() {

    fun getContent() = contentRepository.getContent().map { contentEntityList ->
        contentEntityList.map {
            ContentItem(
                imageUrl = it.imageUrl,
                title = it.title,
                text = it.text,
                dateSaved = it.dateTimeSaved,
                source = when (it.source) {
                    Source.REDDIT.name -> ContentSource.Reddit
                    Source.HACKER_NEWS.name -> ContentSource.HackerNews
                    else ->  ContentSource.Twitter
                }
            )
        }
    }

    companion object {
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val db: AppDatabase by inject(AppDatabase::class.java)
                val repository = ContentRepository(db.contentDao())
                MainScreenViewModel(repository)
            }
        }
    }
}

class ContentRepository(private val contentDao: ContentDao) {

    fun getContent() = contentDao.getAll()
}