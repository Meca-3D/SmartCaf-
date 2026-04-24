package com.ynov.smartcafemobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ynov.smartcafemobile.model.User
import com.ynov.smartcafemobile.navigation.Screen
import com.ynov.smartcafemobile.ui.components.BottomNavBar
import com.ynov.smartcafemobile.ui.theme.CafeOrange
import com.ynov.smartcafemobile.ui.theme.CafeOrangeLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    user: User,
    cartItemCount: Int,
    onLogout: () -> Unit,
    onNavigate: (Screen) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mon Profil", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            BottomNavBar(
                currentScreen = Screen.Profile,
                cartItemCount = cartItemCount,
                onNavigate = onNavigate
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(32.dp))

            // Avatar
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(CafeOrangeLight),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    user.name.firstOrNull()?.uppercaseChar()?.toString() ?: "U",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = CafeOrange
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(
                user.name.ifBlank { "Client" },
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                user.email,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = CafeOrange.copy(alpha = 0.15f),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(
                    user.role,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = CafeOrange,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(Modifier.height(36.dp))

            // Infos
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ProfileRow("👤", "Nom", user.name.ifBlank { "—" })
                    HorizontalDivider()
                    ProfileRow("✉️", "Email", user.email.ifBlank { "—" })
                    HorizontalDivider()
                    ProfileRow("🏷️", "Rôle", user.role)
                }
            }

            Spacer(Modifier.height(32.dp))

            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Se déconnecter", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun ProfileRow(emoji: String, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(emoji, fontSize = 20.sp)
        Spacer(Modifier.width(12.dp))
        Column {
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
        }
    }
}
