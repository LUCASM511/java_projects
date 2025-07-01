package org;

// Exibir o conteúdo da tabela estoque

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class DisplayStock {
	public static void main(String[] args) {
		final String DB_URL = "jdbc:mysql:lius:localhost:PECAS";
		final String SELECT_QUERY = "SELECT id, nome FROM estoque";
		final String UNAME = "root";
		final String UPASS = "0511";

		// usa o try com recursos para conectar-se e consultar o banco de dados
		try (
			Connection connection = DriverManager.getConnection(DB_URL, UNAME, UPASS);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(SELECT_QUERY))

		{
			// obtém os metadados de ResultSet
			ResultSetMetaData metaData = resultSet.getMetaData();
			int numberOfColumns = metaData.getColumnCount();

			System.out.println("TABELA ESTOQUE");

			// exibe os nomes de coluna do ResultSet
			for (int i = 0; i <= numberOfColumns; i++)
				System.out.printf("%-8\t", metaData.getColumnName(i));
			System.out.println();

			// exibe os resultados da consulta
			while (resultSet.next()) {
				for (int i = 1; i <= numberOfColumns; i++)
					System.out.printf("%-8\t", resultSet.getObject(i));
				System.out.println();
			} // os métodos close dos objetos AutoCloseable são chamados agora
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}
	}
}
