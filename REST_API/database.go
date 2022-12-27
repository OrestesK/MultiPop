package main

import (
	"database/sql"
	"fmt"
	_ "github.com/go-sql-driver/mysql"
)

const dbuser =  //database username
const dbpass =  //database user password
const dbname =  //database name

var db = OpenDatabase()

func aamain() {
	var version string
	db.QueryRow("SELECT VERSION()").Scan(&version)
	fmt.Println("Connected to:", version)

	test1 := DBGetUsers()
	fmt.Println(test1)

	test2 := DBGetUserByUserName("TesttUser")
	if test2 != nil {
		fmt.Println(test2.UserPassword)
	}
}
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
		fmt.Println("Err", err.Error())
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
	for rows.Next() {
		var snd Sound

		err := rows.Scan(&snd.U_Id, &snd.PosterUserName, &snd.SoundName, &snd.Description, &snd.DateTime, &snd.Length, &snd.File)
		if err != nil {
			panic(err.Error())
		}

		sounds = append(sounds, snd)
	}
	return sounds
}

func DBGetUserByUserName(username string) *User {
	usr := &User{}

	rows := QueryDatabase("SELECT * FROM Users where Username=?", username)

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

func DBGetSoundsByPosterUserName(username string) []Sound {
	rows := QueryDatabase("SELECT * FROM Sounds WHERE PosterUserName=?", username)
	sounds := []Sound{}
	if rows.Next() {
		var snd Sound

		err := rows.Scan(&snd.U_Id, &snd.PosterUserName, &snd.SoundName, &snd.Description, &snd.DateTime, &snd.Length, &snd.File)
		if err != nil {
			return nil
		}

		sounds = append(sounds, snd)
	} else {
		return nil
	}

	return sounds
}

func DBAddUser(usr User) {
	_, err := db.Exec("INSERT INTO Users (UserName, UserPassword) VALUES (?, ?)", usr.UserName, usr.UserPassword)
	if err != nil {
		panic(err.Error())
	}
}

func DBAddSound(snd Sound) {
	_, err := db.Exec("INSERT INTO Sounds (PosterUserName, SoundName, Description, DateTime, Length, File) VALUES (?, ?, ?, ?, ?, ?)", snd.PosterUserName, snd.SoundName, snd.Description, snd.DateTime, snd.Length, snd.File)
	if err != nil {
		panic(err.Error())
	}
}
