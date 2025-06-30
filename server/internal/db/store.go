package db

import (
	"database/sql"
	"fmt"

	_ "github.com/ncruces/go-sqlite3/driver"
	_ "github.com/ncruces/go-sqlite3/embed"
)

var version string

func InitDB() *sql.DB {
	database, err := sql.Open("sqlite3", "../internal/db/kiroku.db")

	if err != nil {
		fmt.Println("Error connecting to databse ", err)
	}

	_, err = database.Exec(`CREATE TABLE IF NOT EXISTS users (
		id INTEGER PRIMARY KEY AUTOINCREMENT,
		username VARCHAR(256) NOT NULL UNIQUE,
		password VARCHAR(256) NOT NULL UNIQUE,
		created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
		);`)

	_, err = database.Exec(`CREATE TYPE status AS ENUM ('NOT_STARTED', 'ON_GOING', 'COMPLETED');`)

	_, err = database.Exec(`CREATE TABLE IF NOT EXISTS posts (
		id INTEGER PRIMARY KEY AUTOINCREMENT,
		title VARCHAR(256) NOT NULL,
		image VARCHAR(256),
		status status NOT NULL,
		user_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
		created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
		);`)

	if err != nil {
		fmt.Println("Error creating table ", err)
	}

	return database
}
