package main

import (
	"crypto/sha256"
	"encoding/hex"
	"os"
)

func hash(val string) string {
	sha := sha256.New()
	sha.Write([]byte(val))
	return hex.EncodeToString(sha.Sum(nil))
}

// fake main
func testmain() {
	SaveFile(LoadFile("./file_example_MP3_2MG.mp3"), "test.mp3")
}

// stores a file as []byte
func LoadFile(path string) []byte {
	file, _ := os.ReadFile(path)
	return file
}

// saves a previously []byte as a file
func SaveFile(data []byte, name string) {
	os.WriteFile(name, data, 0644)
}

type User struct {
	U_Id         uint   `json:"uid"`
	UserName     string `json:"username"`
	UserPassword string `json:"userpassword"`
}

type Sound struct {
	U_Id           uint   `json:"uid"`
	PosterUserName string `json:"posterusername"`
	SoundName      string `json:"soundname"`
	Description    string `json:"description"`
	DateTime       string `json:"datetime"`
	Length         string `json:"length"`
	File           []byte `json:"file"`
}
