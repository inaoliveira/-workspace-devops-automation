# Configuração do Backend Remoto para salvar o estado na Azure
terraform {
  backend "azurerm" {
    resource_group_name  = "rg-tfstate"
    storage_account_name = "tfstatecurso17230" # Sua storage account criada com sucesso
    container_name       = "tfstate"
    key                  = "prod.terraform.tfstate"
  }
}

provider "azurerm" {
  features {}
}

# 1. Criação do Grupo de Recursos
resource "azurerm_resource_group" "rg" {
  name     = var.resource_group_name
  location = var.location
}

# 2. Rede Virtual (VNet)
resource "azurerm_virtual_network" "vnet" {
  name                = "vnet-automation"
  address_space       = ["10.0.0.0/16"]
  location            = azurerm_resource_group.rg.location
  resource_group_name = azurerm_resource_group.rg.name
}

# 3. Sub-rede (Subnet)
resource "azurerm_subnet" "subnet" {
  name                 = "subnet-automation"
  resource_group_name  = azurerm_resource_group.rg.name
  virtual_network_name = azurerm_virtual_network.vnet.name
  address_prefixes     = ["10.0.1.0/24"]
}

# 4. IP Público (CORRIGIDO: SKU Standard para evitar erro de cota)
resource "azurerm_public_ip" "public_ip" {
  name                = "public-ip-vm"
  location            = azurerm_resource_group.rg.location
  resource_group_name = azurerm_resource_group.rg.name
  allocation_method   = "Static" # Obrigatório para SKU Standard
  sku                 = "Standard" # Mudança essencial para contas de teste/estudante
}

# 5. Interface de Rede (NIC)
resource "azurerm_network_interface" "nic" {
  name                = "nic-vm"
  location            = azurerm_resource_group.rg.location
  resource_group_name = azurerm_resource_group.rg.name

  ip_configuration {
    name                          = "internal"
    subnet_id                     = azurerm_subnet.subnet.id
    private_ip_address_allocation = "Dynamic"
    public_ip_address_id          = azurerm_public_ip.public_ip.id
  }
}

# 6. Grupo de Segurança (NSG) com regras SSH e Swagger
resource "azurerm_network_security_group" "nsg" {
  name                = "nsg-vm"
  location            = azurerm_resource_group.rg.location
  resource_group_name = azurerm_resource_group.rg.name

  security_rule {
    name                       = "SSH"
    priority                   = 1001
    direction                  = "Inbound"
    access                     = "Allow"
    protocol                   = "Tcp"
    source_port_range          = "*"
    destination_port_range     = "22"
    source_address_prefix      = "*"
    destination_address_prefix = "*"
  }

  security_rule {
    name                       = "Swagger"
    priority                   = 1002
    direction                  = "Inbound"
    access                     = "Allow"
    protocol                   = "Tcp"
    source_port_range          = "*"
    destination_port_range     = "8081" # Porta definida no seu diagrama de infra
    source_address_prefix      = "*"
    destination_address_prefix = "*"
  }
}

# 7. Associação do NSG à Interface de Rede
resource "azurerm_network_interface_security_group_association" "nsg_assoc" {
  network_interface_id      = azurerm_network_interface.nic.id
  network_security_group_id = azurerm_network_security_group.nsg.id
}

# 8. Máquina Virtual Linux (VM)
resource "azurerm_linux_virtual_machine" "vm" {
  name                            = "vm-automation"
  resource_group_name             = azurerm_resource_group.rg.name
  location                        = azurerm_resource_group.rg.location
  size                            = "Standard_B1s" # Ideal para laboratório
  admin_username                  = "azureuser"
  admin_password                  = var.admin_password
  disable_password_authentication = false # Permite usar a senha definida no Secret

  network_interface_ids = [
    azurerm_network_interface.nic.id
  ]

  os_disk {
    caching              = "ReadWrite"
    storage_account_type = "Standard_LRS"
  }

  source_image_reference {
    publisher = "Canonical"
    offer     = "0001-com-ubuntu-server-focal"
    sku       = "20_04-lts"
    version   = "latest"
  }
}