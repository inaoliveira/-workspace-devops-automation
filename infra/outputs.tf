output "public_ip_address" {
  value       = azurerm_public_ip.public_ip.ip_address
  description = "Endereço IP público da VM"
}


output "nsg_name" {
  value = azurerm_network_security_group.nsg.name
}

output "resource_group_name" {
  value = azurerm_resource_group.rg.name
}
