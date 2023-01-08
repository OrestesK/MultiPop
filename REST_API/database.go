package main

import (
	"database/sql"
	_ "github.com/go-sql-driver/mysql"
)

const dbuser = ""
const dbpass = ""
const dbname = ""

var db = OpenDatabase()

func OpenDatabase() *sql.DB {
	db, err := sql.Open("mysql", dbuser+":"+dbpass+"@tcp(localhost)/"+dbname)
	if err != nil {
		panic(err.Error())
	}
	return db
}

func QueryDatabase(statement ...string) *sql.Rows {
	var rows *sql.Rows
	var err error

	switch len(statement) {
	case 1:
		rows, err = db.Query(statement[0])
	case 2:
		rows, err = db.Query(statement[0], statement[1])
	}

	if err != nil {
		panic(err.Error())
	}
	return rows
}

func DBGetUsers() []User {
	rows := QueryDatabase("SELECT * FROM Users")
	users := []User{}
	for rows.Next() {
		var usr User

		err := rows.Scan(&usr.U_Id, &usr.UserName, &usr.UserPassword)
		if err != nil {
			panic(err.Error())
		}

		users = append(users, usr)
	}
	return users

}

func DBGetSounds() []Sound {
	rows := QueryDatabase("SELECT * FROM Sounds")
	sounds := []Sound{}
	file := []byte{}

	for rows.Next() {
		var snd Sound

		err := rows.Scan(&snd.U_Id, &snd.PosterUserName, &snd.SoundName, &snd.Description, &snd.DateTime, &snd.Length, &file)
		if err != nil {
			panic(err.Error())
		}

		snd.File = encode64(file)
		sounds = append(sounds, snd)
	}
	return sounds
}

func DBGetUserByUserName(username string) *User {
	rows := QueryDatabase("SELECT * FROM Users where Username=?", username)
	usr := &User{}

	if rows.Next() {
		err := rows.Scan(&usr.U_Id, &usr.UserName, &usr.UserPassword)
		if err != nil {
			return nil
		}
	} else {
		return nil
	}

	return usr
}

func DBGetSoundsByPosterUserName(posterUserName string) []Sound {
	rows := QueryDatabase("SELECT * FROM Sounds WHERE PosterUserName=?", posterUserName)
	sounds := []Sound{}
	file := []byte{}

	for rows.Next() {
		var snd Sound

		err := rows.Scan(&snd.U_Id, &snd.PosterUserName, &snd.SoundName, &snd.Description, &snd.DateTime, &snd.Length, &file)
		if err != nil {
			panic(err.Error())
		}

		snd.File = encode64(file)
		sounds = append(sounds, snd)
	}

	return sounds
}

func DBAddUser(usr User) {
	_, err := db.Exec("INSERT INTO Users (UserName, UserPassword) VALUES (?, ?)", usr.UserName, hash(usr.UserPassword))
	if err != nil {
		panic(err.Error())
	}
}

func DBAddSound(snd Sound) {
	_, err := db.Exec("INSERT INTO Sounds (PosterUserName, SoundName, Description, DateTime, Length, File) VALUES (?, ?, ?, ?, ?, ?)", snd.PosterUserName, snd.SoundName, snd.Description, snd.DateTime, snd.Length, decode64(snd.File))
	if err != nil {
		panic(err.Error())
	}
}
