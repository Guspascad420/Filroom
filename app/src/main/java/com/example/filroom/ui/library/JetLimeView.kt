package com.example.filroom.ui.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.pushpal.jetlime.data.JetLimeItem
import com.pushpal.jetlime.data.config.JetLimeViewConfig
import com.pushpal.jetlime.ui.JetLimeItemView

@Composable
fun JetLimeView(
  modifier: Modifier = Modifier,
  jetLimeItems: List<JetLimeItem>,
  jetLimeViewConfig: JetLimeViewConfig,
  listState: LazyListState = rememberLazyListState()
) {
  LazyColumn(
    modifier = modifier,
    state = listState,
    verticalArrangement = Arrangement.spacedBy(jetLimeViewConfig.itemSpacing)
  ) {
    items(count = jetLimeItems.size) { pos ->
      jetLimeItems[pos].let { item ->
        val jetLimeItem = remember { mutableStateOf(item) }
        JetLimeItemView(
          title = jetLimeItem.value.title,
          description = jetLimeItem.value.description,
          content = jetLimeItem.value.content,
          jetLimeItemConfig = jetLimeItem.value.jetLimeItemConfig.apply { position = pos },
          jetLimeViewConfig = jetLimeViewConfig,
          totalItems = jetLimeItems.size
        )
      }
    }
  }
}