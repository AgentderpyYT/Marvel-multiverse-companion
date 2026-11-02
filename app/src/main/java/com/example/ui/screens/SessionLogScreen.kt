package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material.icons.filled.Public
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
import com.example.data.models.SessionEntryType
import com.example.data.models.SessionLogEntry
import com.example.ui.CompanionViewModel
import com.example.ui.components.ComicHeaderBanner
import com.example.ui.theme.BangersFont
import com.example.ui.theme.ComicBorder
import com.example.ui.theme.ComicCard
import com.example.ui.theme.ComicCardElevated
import com.example.ui.theme.ComicDarkBg
import com.example.ui.theme.ComicTextPrimary
import com.example.ui.theme.ComicTextSecondary
import com.example.ui.theme.FocusCyan
import com.example.ui.theme.HealthRed
import com.example.ui.theme.MarvelGold
import com.example.ui.theme.MarvelRed

@Composable
fun SessionLogScreen(
  viewModel: CompanionViewModel,
  onOpenWebView: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val sessionLogs by viewModel.sessionLogs.collectAsState()

  var showAddNoteDialog by remember { mutableStateOf(false) }
  var showExportDialog by remember { mutableStateOf(false) }
  var showImportDialog by remember { mutableStateOf(false) }
  var exportedJsonContent by remember { mutableStateOf("") }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(ComicDarkBg)
      .testTag("session_log_screen"),
    contentPadding = PaddingValues(bottom = 80.dp)
  ) {
    item {
      ComicHeaderBanner(
        title = "MISSION LOG & DATA",
        subtitle = "Chronicles, Dice Attachments & Full JSON Backup",
        badgeText = "${sessionLogs.size} ENTRIES"
      )
    }

    // Backup & Standalone Tools Bar
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 6.dp)
          .border(1.dp, ComicBorder, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = ComicCard),
        shape = RoundedCornerShape(12.dp)
      ) {
        Column(modifier = Modifier.padding(12.dp)) {
          Text(
            text = "DATA MANAGEMENT & BACKUP",
            fontFamily = BangersFont,
            fontSize = 15.sp,
            color = MarvelGold
          )
          Spacer(modifier = Modifier.height(8.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Button(
              onClick = {
                exportedJsonContent = viewModel.exportAllDataJson()
                showExportDialog = true
              },
              colors = ButtonDefaults.buttonColors(containerColor = MarvelRed),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier
                .weight(1f)
                .testTag("export_json_button")
            ) {
              Icon(imageVector = Icons.Default.FileUpload, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("EXPORT JSON", fontFamily = BangersFont, fontSize = 13.sp)
            }

            Button(
              onClick = { showImportDialog = true },
              colors = ButtonDefaults.buttonColors(containerColor = ComicCardElevated, contentColor = MarvelGold),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier
                .weight(1f)
                .testTag("import_json_button")
            ) {
              Icon(imageVector = Icons.Default.FileDownload, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("IMPORT JSON", fontFamily = BangersFont, fontSize = 13.sp)
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          // Standalone Web App Launcher
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = ComicCardElevated,
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onOpenWebView() }
              .border(0.5.dp, MarvelGold.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
              .padding(10.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Public, contentDescription = null, tint = FocusCyan, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                  Text(
                    text = "STANDALONE WEB COMPANION (HTML)",
                    fontFamily = BangersFont,
                    fontSize = 14.sp,
                    color = Color.White
                  )
                  Text(
                    text = "Single-file browser app with localStorage & comic CSS",
                    style = MaterialTheme.typography.bodySmall,
                    color = ComicTextSecondary
                  )
                }
              }
              Text(
                text = "OPEN",
                fontFamily = BangersFont,
                fontSize = 13.sp,
                color = MarvelGold
              )
            }
          }
        }
      }
    }

    // New Note and Clear Actions
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Button(
          onClick = { showAddNoteDialog = true },
          colors = ButtonDefaults.buttonColors(containerColor = MarvelRed),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.testTag("add_note_button")
        ) {
          Icon(imageVector = Icons.Default.NoteAdd, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("NEW MISSION NOTE", fontFamily = BangersFont, fontSize = 14.sp)
        }

        if (sessionLogs.isNotEmpty()) {
          TextButton(
            onClick = { viewModel.clearSessionLogs() },
            modifier = Modifier.testTag("clear_logs_button")
          ) {
            Icon(imageVector = Icons.Default.DeleteSweep, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Clear All", color = Color.Gray, fontSize = 12.sp)
          }
        }
      }
    }

    // Session Log Timeline
    if (sessionLogs.isEmpty()) {
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "No notes or pinned rolls yet.\nJot down campaign beats or pin results from the Dice Roller & Generators!",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
          )
        }
      }
    } else {
      items(sessionLogs) { entry ->
        val (icon, typeColor) = when (entry.type) {
          SessionEntryType.NOTE -> Icons.Default.BookmarkBorder to MarvelGold
          SessionEntryType.ROLL -> Icons.Default.Casino to MarvelRed
          SessionEntryType.GENERATOR -> Icons.Default.Add to FocusCyan
          SessionEntryType.CHARACTER_EVENT -> Icons.Default.Add to Color(0xFF10B981)
        }

        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 5.dp)
            .border(0.5.dp, ComicBorder, RoundedCornerShape(10.dp)),
          colors = CardDefaults.cardColors(containerColor = ComicCard),
          shape = RoundedCornerShape(10.dp)
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icon, contentDescription = null, tint = typeColor, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = entry.title,
                  fontFamily = BangersFont,
                  fontSize = 17.sp,
                  color = Color.White
                )
              }

              IconButton(
                onClick = { viewModel.deleteSessionLog(entry.id) },
                modifier = Modifier.size(24.dp)
              ) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color.Gray, modifier = Modifier.size(15.dp))
              }
            }

            Text(
              text = entry.formattedTime,
              style = MaterialTheme.typography.bodySmall,
              color = ComicTextSecondary,
              modifier = Modifier.padding(vertical = 2.dp)
            )

            Surface(
              shape = RoundedCornerShape(8.dp),
              color = ComicCardElevated,
              modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
                .border(0.5.dp, ComicBorder, RoundedCornerShape(8.dp))
                .padding(8.dp)
            ) {
              Text(
                text = entry.content,
                style = MaterialTheme.typography.bodyMedium,
                color = ComicTextPrimary,
                lineHeight = 18.sp
              )
            }
          }
        }
      }
    }
  }

  // Add Note Dialog
  if (showAddNoteDialog) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    AlertDialog(
      onDismissRequest = { showAddNoteDialog = false },
      title = { Text("NEW MISSION NOTE", fontFamily = BangersFont, fontSize = 20.sp, color = MarvelGold) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Note Title (e.g. Infiltration Plan)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Details & Campaign Events") },
            maxLines = 6,
            modifier = Modifier
              .fillMaxWidth()
              .height(140.dp)
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (title.isNotBlank() || content.isNotBlank()) {
              viewModel.addSessionNote(
                title = if (title.isBlank()) "Mission Log Entry" else title,
                content = content
              )
            }
            showAddNoteDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = MarvelRed)
        ) {
          Text("SAVE NOTE", fontFamily = BangersFont)
        }
      },
      dismissButton = {
        TextButton(onClick = { showAddNoteDialog = false }) { Text("CANCEL", color = Color.Gray, fontFamily = BangersFont) }
      },
      containerColor = ComicCard
    )
  }

  // Export JSON Dialog
  if (showExportDialog) {
    AlertDialog(
      onDismissRequest = { showExportDialog = false },
      title = { Text("EXPORT ALL DATA (JSON)", fontFamily = BangersFont, fontSize = 20.sp, color = MarvelGold) },
      text = {
        Column {
          Text(
            text = "Copy this JSON to back up all your heroes, rules, session logs, and dice history, or transfer to another device.",
            style = MaterialTheme.typography.bodySmall,
            color = Color.LightGray,
            modifier = Modifier.padding(bottom = 8.dp)
          )
          OutlinedTextField(
            value = exportedJsonContent,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
              .fillMaxWidth()
              .height(280.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedTextColor = ComicTextPrimary,
              unfocusedTextColor = ComicTextPrimary,
              focusedBorderColor = MarvelRed,
              unfocusedBorderColor = ComicBorder
            )
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Marvel D616 Companion Data", exportedJsonContent)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "Copied backup JSON to clipboard!", Toast.LENGTH_SHORT).show()
            showExportDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = MarvelRed)
        ) {
          Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("COPY JSON", fontFamily = BangersFont)
        }
      },
      dismissButton = {
        TextButton(onClick = { showExportDialog = false }) { Text("CLOSE", color = Color.Gray, fontFamily = BangersFont) }
      },
      containerColor = ComicCard
    )
  }

  // Import JSON Dialog
  if (showImportDialog) {
    var importJsonText by remember { mutableStateOf("") }
    var importError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
      onDismissRequest = { showImportDialog = false },
      title = { Text("IMPORT DATA (JSON)", fontFamily = BangersFont, fontSize = 20.sp, color = MarvelGold) },
      text = {
        Column {
          Text(
            text = "Paste a previously exported JSON backup here to restore your characters, rules, and session logs.",
            style = MaterialTheme.typography.bodySmall,
            color = Color.LightGray,
            modifier = Modifier.padding(bottom = 8.dp)
          )

          if (importError != null) {
            Text(
              text = "Error: $importError",
              color = HealthRed,
              style = MaterialTheme.typography.bodySmall,
              modifier = Modifier.padding(bottom = 4.dp)
            )
          }

          OutlinedTextField(
            value = importJsonText,
            onValueChange = {
              importJsonText = it
              importError = null
            },
            placeholder = { Text("Paste JSON here...", color = Color.Gray) },
            modifier = Modifier
              .fillMaxWidth()
              .height(260.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedTextColor = ComicTextPrimary,
              unfocusedTextColor = ComicTextPrimary,
              focusedBorderColor = MarvelRed,
              unfocusedBorderColor = ComicBorder
            )
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (importJsonText.isNotBlank()) {
              val success = viewModel.importAllDataJson(importJsonText)
              if (success) {
                Toast.makeText(context, "Data successfully imported!", Toast.LENGTH_SHORT).show()
                showImportDialog = false
              } else {
                importError = "Failed to parse JSON. Please check formatting."
              }
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = MarvelRed)
        ) {
          Text("RESTORE BACKUP", fontFamily = BangersFont)
        }
      },
      dismissButton = {
        TextButton(onClick = { showImportDialog = false }) { Text("CANCEL", color = Color.Gray, fontFamily = BangersFont) }
      },
      containerColor = ComicCard
    )
  }
}
