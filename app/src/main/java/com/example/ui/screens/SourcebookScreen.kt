package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.DefaultRulesData
import com.example.data.models.RuleEntry
import com.example.ui.CompanionViewModel
import com.example.ui.components.ComicHeaderBanner
import com.example.ui.theme.BangersFont
import com.example.ui.theme.ComicBorder
import com.example.ui.theme.ComicCard
import com.example.ui.theme.ComicCardElevated
import com.example.ui.theme.ComicDarkBg
import com.example.ui.theme.ComicTextPrimary
import com.example.ui.theme.ComicTextSecondary
import com.example.ui.theme.HealthRed
import com.example.ui.theme.MarvelGold
import com.example.ui.theme.MarvelRed
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SourcebookScreen(
  viewModel: CompanionViewModel,
  modifier: Modifier = Modifier
) {
  val rules by viewModel.rules.collectAsState()
  var searchQuery by remember { mutableStateOf("") }
  var selectedCategory by remember { mutableStateOf("All") }

  var expandedRuleId by remember { mutableStateOf<String?>(null) }
  var showAddRuleDialog by remember { mutableStateOf(false) }
  var editingRule by remember { mutableStateOf<RuleEntry?>(null) }
  var showJsonEditorDialog by remember { mutableStateOf(false) }

  val categories = listOf(
    "All",
    "Core Rules & d616",
    "Combat & Actions",
    "Damage & Health",
    "Archetypes",
    "Powers",
    "Traits",
    "Tags",
    "Ranks & Scale",
    "Equipment"
  )

  val filteredRules = rules.filter { rule ->
    val matchesCategory = selectedCategory == "All" || rule.category == selectedCategory
    val matchesSearch = searchQuery.isBlank() ||
      rule.title.contains(searchQuery, ignoreCase = true) ||
      rule.subtitle.contains(searchQuery, ignoreCase = true) ||
      rule.summary.contains(searchQuery, ignoreCase = true) ||
      rule.details.contains(searchQuery, ignoreCase = true) ||
      rule.tags.any { it.contains(searchQuery, ignoreCase = true) }
    matchesCategory && matchesSearch
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(ComicDarkBg)
      .testTag("sourcebook_screen"),
    contentPadding = PaddingValues(bottom = 80.dp)
  ) {
    item {
      ComicHeaderBanner(
        title = "SOURCEBOOK & RULES",
        subtitle = "Searchable D616 Compendium & Custom JSON Rules",
        badgeText = "${rules.size} ENTRIES"
      )
    }

    // Community / User-supplied Disclaimer Note
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 4.dp)
          .border(1.dp, MarvelGold.copy(alpha = 0.5f), RoundedCornerShape(10.dp)),
        colors = CardDefaults.cardColors(containerColor = ComicCardElevated),
        shape = RoundedCornerShape(10.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = MarvelGold, modifier = Modifier.size(20.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = DefaultRulesData.USER_SUPPLIED_NOTE,
            style = MaterialTheme.typography.bodySmall,
            color = ComicTextSecondary,
            lineHeight = 15.sp
          )
        }
      }
    }

    // Search and Action Bar
    item {
      Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)) {
        OutlinedTextField(
          value = searchQuery,
          onValueChange = { searchQuery = it },
          placeholder = { Text("Search rules, actions, powers, tags...", color = Color.Gray, fontSize = 13.sp) },
          leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = MarvelGold) },
          trailingIcon = {
            if (searchQuery.isNotEmpty()) {
              IconButton(onClick = { searchQuery = "" }) {
                Text("×", fontFamily = BangersFont, fontSize = 20.sp, color = Color.Gray)
              }
            }
          },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("sourcebook_search_input"),
          colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = ComicTextPrimary,
            unfocusedTextColor = ComicTextPrimary,
            focusedBorderColor = MarvelRed,
            unfocusedBorderColor = ComicBorder
          )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Categories Ribbon
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          categories.forEach { cat ->
            val isSelected = selectedCategory == cat
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSelected) MarvelRed else ComicCard)
                .border(1.dp, if (isSelected) MarvelGold else ComicBorder, RoundedCornerShape(8.dp))
                .clickable { selectedCategory = cat }
                .padding(horizontal = 10.dp, vertical = 6.dp)
                .testTag("category_chip_$cat")
            ) {
              Text(
                text = cat,
                fontFamily = BangersFont,
                fontSize = 12.sp,
                color = if (isSelected) Color.White else ComicTextSecondary
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Buttons: Add Custom Rule & JSON Editor
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Button(
            onClick = { showAddRuleDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = MarvelRed),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
              .weight(1f)
              .testTag("add_custom_rule_button")
          ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("ADD RULE", fontFamily = BangersFont, fontSize = 13.sp)
          }

          Button(
            onClick = { showJsonEditorDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = ComicCardElevated, contentColor = MarvelGold),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
              .weight(1f)
              .testTag("json_editor_button")
          ) {
            Icon(imageVector = Icons.Default.Code, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("EDIT JSON", fontFamily = BangersFont, fontSize = 13.sp)
          }
        }
      }
    }

    // Rules List
    if (filteredRules.isEmpty()) {
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "No matching rules found.\nTry a different search term or add your own custom rule.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
          )
        }
      }
    } else {
      items(filteredRules) { rule ->
        val isExpanded = expandedRuleId == rule.id

        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 5.dp)
            .border(1.dp, if (isExpanded) MarvelGold else ComicBorder, RoundedCornerShape(10.dp)),
          colors = CardDefaults.cardColors(containerColor = ComicCard),
          shape = RoundedCornerShape(10.dp)
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { expandedRuleId = if (isExpanded) null else rule.id }
              .padding(12.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Box(
                  modifier = Modifier
                    .background(MarvelRed.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                  Text(
                    text = rule.category.uppercase(),
                    fontSize = 9.sp,
                    fontFamily = BangersFont,
                    color = MarvelGold
                  )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = rule.title,
                  fontFamily = BangersFont,
                  fontSize = 18.sp,
                  color = Color.White
                )
                if (rule.subtitle.isNotBlank()) {
                  Text(
                    text = rule.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray
                  )
                }
              }

              Icon(
                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = null,
                tint = MarvelGold
              )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
              text = rule.summary,
              style = MaterialTheme.typography.bodyMedium,
              color = ComicTextSecondary
            )

            AnimatedVisibility(visible = isExpanded) {
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(top = 10.dp)
              ) {
                if (rule.details.isNotBlank()) {
                  Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = ComicCardElevated,
                    modifier = Modifier
                      .fillMaxWidth()
                      .border(0.5.dp, ComicBorder, RoundedCornerShape(8.dp))
                      .padding(10.dp)
                  ) {
                    Text(
                      text = rule.details,
                      style = MaterialTheme.typography.bodyMedium,
                      color = ComicTextPrimary,
                      lineHeight = 20.sp
                    )
                  }
                }

                if (rule.tags.isNotEmpty()) {
                  Spacer(modifier = Modifier.height(8.dp))
                  FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                  ) {
                    rule.tags.forEach { tag ->
                      Box(
                        modifier = Modifier
                          .background(ComicDarkBg, RoundedCornerShape(4.dp))
                          .border(0.5.dp, ComicBorder, RoundedCornerShape(4.dp))
                          .padding(horizontal = 6.dp, vertical = 2.dp)
                      ) {
                        Text(text = "#$tag", fontSize = 10.sp, color = MarvelGold)
                      }
                    }
                  }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.End
                ) {
                  TextButton(onClick = { editingRule = rule }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null, tint = MarvelGold, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Edit", color = MarvelGold, fontFamily = BangersFont)
                  }
                  TextButton(onClick = { viewModel.deleteRule(rule.id) }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null, tint = HealthRed, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Delete", color = HealthRed, fontFamily = BangersFont)
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  // Add / Edit Rule Dialog
  if (showAddRuleDialog || editingRule != null) {
    val isEdit = editingRule != null
    var title by remember { mutableStateOf(editingRule?.title ?: "") }
    var subtitle by remember { mutableStateOf(editingRule?.subtitle ?: "") }
    var category by remember { mutableStateOf(editingRule?.category ?: selectedCategory.ifEmpty { "Core Rules" }) }
    var summary by remember { mutableStateOf(editingRule?.summary ?: "") }
    var details by remember { mutableStateOf(editingRule?.details ?: "") }
    var tagsStr by remember { mutableStateOf(editingRule?.tags?.joinToString(", ") ?: "") }

    AlertDialog(
      onDismissRequest = {
        showAddRuleDialog = false
        editingRule = null
      },
      title = {
        Text(
          text = if (isEdit) "EDIT RULE ENTRY" else "ADD CUSTOM RULE",
          fontFamily = BangersFont,
          fontSize = 20.sp,
          color = MarvelGold
        )
      },
      text = {
        LazyColumn(
          modifier = Modifier
            .fillMaxWidth()
            .height(380.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          item { OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") }, singleLine = true) }
          item { OutlinedTextField(value = subtitle, onValueChange = { subtitle = it }, label = { Text("Subtitle") }, singleLine = true) }
          item { OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Category") }, singleLine = true) }
          item { OutlinedTextField(value = summary, onValueChange = { summary = it }, label = { Text("Short Summary") }, maxLines = 3) }
          item { OutlinedTextField(value = details, onValueChange = { details = it }, label = { Text("Detailed Mechanics / Text") }, maxLines = 6) }
          item { OutlinedTextField(value = tagsStr, onValueChange = { tagsStr = it }, label = { Text("Tags (comma separated)") }, singleLine = true) }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (title.isNotBlank()) {
              val tagsList = tagsStr.split(",").map { it.trim() }.filter { it.isNotEmpty() }
              val updated = RuleEntry(
                id = editingRule?.id ?: UUID.randomUUID().toString(),
                category = category.ifBlank { "Core Rules" },
                title = title,
                subtitle = subtitle,
                summary = summary,
                details = details,
                tags = tagsList
              )
              viewModel.saveRule(updated)
            }
            showAddRuleDialog = false
            editingRule = null
          },
          colors = ButtonDefaults.buttonColors(containerColor = MarvelRed)
        ) {
          Text("SAVE RULE", fontFamily = BangersFont)
        }
      },
      dismissButton = {
        TextButton(onClick = {
          showAddRuleDialog = false
          editingRule = null
        }) {
          Text("CANCEL", color = Color.Gray, fontFamily = BangersFont)
        }
      },
      containerColor = ComicCard
    )
  }

  // JSON Rulebook Editor Dialog
  if (showJsonEditorDialog) {
    val context = LocalContext.current
    var jsonText by remember {
      val array = JSONArray()
      for (r in rules) {
        val obj = JSONObject()
        obj.put("id", r.id)
        obj.put("category", r.category)
        obj.put("title", r.title)
        obj.put("subtitle", r.subtitle)
        obj.put("summary", r.summary)
        obj.put("details", r.details)
        val tArr = JSONArray()
        r.tags.forEach { tArr.put(it) }
        obj.put("tags", tArr)
        array.put(obj)
      }
      mutableStateOf(array.toString(2))
    }
    var parseError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
      onDismissRequest = { showJsonEditorDialog = false },
      title = { Text("EDITABLE JSON RULEBOOK", fontFamily = BangersFont, fontSize = 20.sp, color = MarvelGold) },
      text = {
        Column(modifier = Modifier.fillMaxWidth()) {
          Text(
            text = "Paste or edit your custom rule entries in JSON. Format: array of { id, category, title, subtitle, summary, details, tags }.",
            style = MaterialTheme.typography.bodySmall,
            color = Color.LightGray,
            modifier = Modifier.padding(bottom = 6.dp)
          )

          if (parseError != null) {
            Text(
              text = "Error: $parseError",
              color = HealthRed,
              style = MaterialTheme.typography.bodySmall,
              modifier = Modifier.padding(bottom = 4.dp)
            )
          }

          OutlinedTextField(
            value = jsonText,
            onValueChange = {
              jsonText = it
              parseError = null
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(300.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedTextColor = ComicTextPrimary,
              unfocusedTextColor = ComicTextPrimary,
              focusedBorderColor = MarvelRed,
              unfocusedBorderColor = ComicBorder
            )
          )

          Spacer(modifier = Modifier.height(6.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            TextButton(
              onClick = {
                viewModel.resetRulesToDefault()
                showJsonEditorDialog = false
                Toast.makeText(context, "Rules reset to default", Toast.LENGTH_SHORT).show()
              }
            ) {
              Icon(imageVector = Icons.Default.Refresh, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Reset to Defaults", color = Color.Gray, fontSize = 11.sp)
            }
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            try {
              val array = JSONArray(jsonText)
              for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val tags = mutableListOf<String>()
                val tArr = obj.optJSONArray("tags")
                if (tArr != null) {
                  for (j in 0 until tArr.length()) tags.add(tArr.getString(j))
                }
                val rule = RuleEntry(
                  id = obj.optString("id", UUID.randomUUID().toString()),
                  category = obj.optString("category", "Core Rules"),
                  title = obj.getString("title"),
                  subtitle = obj.optString("subtitle", ""),
                  summary = obj.optString("summary", ""),
                  details = obj.optString("details", ""),
                  tags = tags
                )
                viewModel.saveRule(rule)
              }
              showJsonEditorDialog = false
              Toast.makeText(context, "Rules successfully saved!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
              parseError = e.message ?: "Invalid JSON syntax"
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = MarvelRed)
        ) {
          Text("APPLY JSON", fontFamily = BangersFont)
        }
      },
      dismissButton = {
        TextButton(onClick = { showJsonEditorDialog = false }) { Text("CANCEL", color = Color.Gray, fontFamily = BangersFont) }
      },
      containerColor = ComicCard
    )
  }
}
