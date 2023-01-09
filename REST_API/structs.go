package main

import (
	"crypto/sha256"
	"encoding/base64"
	"encoding/hex"
	"os"
)

// hashes strings in sha256 (not used as hashing is client-side)
func hash(val string) string {
	sha := sha256.New()
	sha.Write([]byte(val))
	return hex.EncodeToString(sha.Sum(nil))
}

// encodes []byte to base64 String
func encode64(val []byte) string {
	file := base64.StdEncoding.EncodeToString(val)
	return file
}

// decodes base64 String to []byte
func decode64(val string) []byte {
	file, _ := base64.StdEncoding.DecodeString(val)
	return file
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
	UserName     string `json:"username" binding:"required"`
	UserPassword string `json:"userpassword" binding:"required"`
}

type Sound struct {
	U_Id           uint   `json:"uid"`
	PosterUserName string `json:"posterusername" binding:"required"`
	SoundName      string `json:"soundname" binding:"required"`
	Description    string `json:"description"`
	DateTime       string `json:"datetime"`
	Length         int    `json:"length"`
	File           string `json:"file" binding:"required"`
}
