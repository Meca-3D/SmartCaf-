package com.ynov.smartcafemobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ynov.smartcafemobile.ui.theme.Beige
import com.ynov.smartcafemobile.ui.theme.BrandText
import com.ynov.smartcafemobile.ui.theme.DarkGreen
import com.ynov.smartcafemobile.ui.theme.Gold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Smart Café", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Gold)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour", tint = Color.White)
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier.padding(end = 12.dp).size(36.dp).clip(CircleShape).background(DarkGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkGreen)
            )
        },
        containerColor = Beige
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(16.dp))
            Text(
                "Aide",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = BrandText
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Gold.copy(alpha = 0.3f))

            HelpItem(
                question = "Comment passer une commande ?",
                answer = "Parcourez la carte depuis l'accueil, ajoutez les produits souhaités à votre panier puis validez votre commande."
            )
            HelpItem(
                question = "Comment modifier mon profil ?",
                answer = "Rendez-vous dans Mon compte > Mon profil pour modifier vos informations personnelles."
            )
            HelpItem(
                question = "Comment utiliser le QR code ?",
                answer = "Lors d'une commande sur place, scannez le QR code présent sur votre table pour l'associer à votre commande."
            )
            HelpItem(
                question = "Comment contacter le support ?",
                answer = "Pour toute question, contactez-nous à l'adresse support@smartcafe.fr ou directement auprès du personnel."
            )
        }
    }
}

@Composable
private fun HelpItem(question: String, answer: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.Star, contentDescription = null, tint = Gold, modifier = Modifier.size(18.dp).padding(top = 2.dp))
                Text(question, fontWeight = FontWeight.SemiBold, color = BrandText)
            }
            Spacer(Modifier.height(8.dp))
            Text(answer, color = BrandText.copy(alpha = 0.75f), fontSize = 14.sp, modifier = Modifier.padding(start = 26.dp))
        }
    }
}
