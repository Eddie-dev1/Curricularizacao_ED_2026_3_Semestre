

Create database MapShelf;

Use MapShelf;

CREATE TABLE `layout` (
  `Numero_gondola` int NOT NULL,
  `Corredor_gondola` int NOT NULL,
  `Fileira_gondola` int NOT NULL,
  PRIMARY KEY (`Numero_gondola`)
);

CREATE TABLE `produto` (
  `Id_prod` int NOT NULL AUTO_INCREMENT,
  `Nome_prod` varchar(100) NOT NULL,
  `Tipo_prod` varchar(100) NOT NULL,
  `Marca_prod` varchar(100) NOT NULL,
  `Volume_prod` decimal(10,2) DEFAULT NULL,
  `Peso_prod` decimal(10,2) NOT NULL,
  `Preco_prod` decimal(10,2) NOT NULL,
  `Numero_gondola` int NOT NULL,
  `Quantidade_prod` int NOT NULL,
  `Prateleira_prod` int NOT NULL,
  PRIMARY KEY (`Id_prod`),
  KEY `fk1` (`Numero_gondola`),
  CONSTRAINT `fk1` FOREIGN KEY (`Numero_gondola`) REFERENCES `layout` (`Numero_gondola`)
);

CREATE TABLE `usuario` (
  `Id_usuario` int NOT NULL AUTO_INCREMENT,
  `Senha_usuario` varchar(50) NOT NULL,
  `Email_usuario` varchar(100) NOT NULL,
  `Tipo_usuario` enum('cliente','admin') NOT NULL DEFAULT 'cliente',
  `Nome_usuario` varchar(100) NOT NULL,
  PRIMARY KEY (`Id_usuario`)
)


