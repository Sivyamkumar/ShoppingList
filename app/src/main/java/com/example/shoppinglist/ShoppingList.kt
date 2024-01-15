package com.example.shoppinglist.ui.theme

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
fun shopping_list() {
    var sItems by remember { mutableStateOf(listOf<ShoppingItem>())}
    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }

    val context = LocalContext.current


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .background(Color.Transparent)
                .padding(top = 20.dp),
            colors = ButtonDefaults.buttonColors(Color.Black)
        ) {
            Text(
                text = "Add Item",
                color = Color.Cyan,
                fontSize = 16.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .background(Color.Transparent)
                    .padding(10.dp, 10.dp, 10.dp, 10.dp)
            )
        }
//        Alternative of Recycler View in Jetpack Compose.
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(sItems){
                item ->
                if(item.isEditing){
                    ShoppingItemEditor(item=item, onEditComplete = {
                        editedName,editedQuantity ->
                        sItems = sItems.map{it.copy(isEditing = false)}
                        val editedItem= sItems.find { it.id==item.id}
                        editedItem?.let {
                            it.name= editedName
                            it.quantity= editedQuantity
                        }
                    })
                }else{
                    shoppingListItem(item=item,
                        onEditClick = {
                            sItems = sItems.map{it.copy(isEditing = it.id == item.id)}
                        },
                        onDeleteClick = {
                            sItems = sItems-item
                        })
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(onDismissRequest = {
            showDialog = false
        },
            confirmButton = {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween){
                    Button(onClick = {
                        if(itemName.isNotBlank() && itemQuantity.isNotBlank()){
                            val newItem =  ShoppingItem(
                                id = sItems.size+1,
                                name = itemName,
                                quantity =  itemQuantity.toInt()
                            )
                            sItems += newItem
                            itemName=""
                            itemQuantity=""
                            showDialog=false
                        }else{
                            var count = 0;
                            if(itemName.isBlank() && itemQuantity.isBlank()){
                                Toast.makeText(context,"Invalid Item Name & Number",Toast.LENGTH_LONG).show()
                                count+=1
                            }
                            if(itemName.isBlank() && count==0){
                                Toast.makeText(context,"Invalid Item Name",Toast.LENGTH_LONG).show()
                            }else if(itemQuantity.isBlank() && count ==0){
                                Toast.makeText(context,"Invalid Item Number",Toast.LENGTH_LONG).show()
                            }
                        }
                    }) {
                        Text(
                            text = "Add",
                            fontSize = 12.sp,
                            modifier = Modifier.padding(2.dp,2.dp,2.dp,2.dp))
                    }
                    Button(onClick = {showDialog=false}) {
                        Text(
                            text = "Cancel",
                            fontSize = 12.sp,
                            modifier = Modifier.padding(2.dp,2.dp,2.dp,2.dp))
                    }
                }
            },
            title = {
                Text(
                text = "Add Shopping Item",
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Default,
                fontStyle = FontStyle.Italic
            )},
            text = {
                Column {
                    OutlinedTextField(
                        value = itemName,
                        onValueChange = {itemName = it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                    OutlinedTextField(
                        value = itemQuantity,
                        onValueChange = {itemQuantity = it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            })
    }
}


// Composable for designing the main screen or screen which shows list of objects.
@Composable
fun shoppingListItem(
    item:ShoppingItem,
//    Lambda Function in used below
    onEditClick: ()-> Unit,
    onDeleteClick: ()  -> Unit
){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                border = BorderStroke(5.dp, Color.Cyan),
                shape = RoundedCornerShape(20)
            ),
        horizontalArrangement = Arrangement.Absolute.SpaceBetween
    ){
        Text(text = item.name,modifier = Modifier.padding(8.dp))
        Text(text = "Qty: ${item.quantity}",modifier = Modifier.padding(8.dp))
        Row(modifier = Modifier.padding(8.dp)){
            IconButton(onClick = { onEditClick() },) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
            IconButton(onClick = { onDeleteClick() },) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}

//Composable for editing the content of the list.
@Composable
fun ShoppingItemEditor(item: ShoppingItem, onEditComplete: (String,Int) -> Unit){
    var editedName by remember { mutableStateOf(item.name) }
    var editedQuantity by remember { mutableStateOf(item.quantity.toString()) }
    var isEditing by remember { mutableStateOf(item.isEditing) }

    Row(modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly){
        Column {
            BasicTextField(value = editedName,
                onValueChange = {editedName = it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp))

            BasicTextField(value = editedQuantity,
                onValueChange = {editedQuantity = it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp))
        }
        Button(
            onClick = {
                isEditing=false
                onEditComplete(editedName,editedQuantity.toIntOrNull() ?: 1)
            }) {
            Text(text = "Save")
        }
    }
}

data class ShoppingItem(val id:Int,
                        var name: String,
                        var quantity: Int,
                        var isEditing: Boolean = false

)

@Preview(showBackground = true)
@Composable
fun GreetingPreview(){
    shopping_list()
}