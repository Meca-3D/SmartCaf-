package com.ynov.smartcafemobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
fun LegalScreen(onBack: () -> Unit) {
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
                "Mentions légales",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = BrandText
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Gold.copy(alpha = 0.3f))

            LegalSection(
                title = "Éditeur",
                content = "Smart Café SAS\nCapital social : 50 000 €\nSiège social : 12 rue de la Paix, 75001 Paris\nSIRET : 123 456 789 00012\nEmail : contact@smartcafe.fr"
            )
            LegalSection(
                title = "Hébergement",
                content = "L'application est hébergée par des serveurs sécurisés conformes aux réglementations en vigueur."
            )
            LegalSection(
                title = "Données personnelles",
                content = "Conformément au RGPD, vous disposez d'un droit d'accès, de rectification et de suppression de vos données personnelles. Pour exercer ces droits, contactez-nous à l'adresse : privacy@smartcafe.fr"
            )
            LegalSection(
                title = "Propriété intellectuelle",
                content = "Tous les contenus présents dans cette application (textes, images, logo) sont la propriété exclusive de Smart Café SAS et sont protégés par les lois relatives à la propriété intellectuelle."
            )
            LegalSection(
                title = "Contact",
                content = "Pour toute question relative aux présentes mentions légales, vous pouvez nous contacter à : legal@smartcafe.fr",
                isLast = true
            )
        }
    }
}

@Composable
private fun LegalSection(title: String, content: String, isLast: Boolean = false) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(title, fontWeight = FontWeight.Bold, color = Gold, fontSize = 15.sp)
        Spacer(Modifier.height(6.dp))
        Text(content, color = BrandText.copy(alpha = 0.8f), fontSize = 14.sp, lineHeight = 20.sp)
        if (!isLast) {
            HorizontalDivider(modifier = Modifier.padding(top = 16.dp), color = Gold.copy(alpha = 0.2f))
        }
    }
}
