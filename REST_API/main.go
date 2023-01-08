package main

import (
	"github.com/gin-gonic/gin"
	"net/http"
	"os"
)

// main function
func main() {
	if len(os.Args) < 2 || os.Args[1] != "-d" {
		gin.SetMode(gin.ReleaseMode)
	}

	router := gin.Default()

	router.GET("/users", getUsers)
	router.GET("/users/:username", getUserByUserName)
	router.POST("/users", postUser)

	router.GET("/sounds", getSounds)
	router.GET("/sounds/:posterusername", getSoundsByPosterUserName)
	router.POST("/sounds", postSound)

	router.RunTLS("0.0.0.0:8080", "https/cert.crt", "https/key.key")
}

// return GETS all Users
func getUsers(c *gin.Context) {
	users := DBGetUsers()
	if users != nil || len(users) != 0 {
		c.JSON(http.StatusOK, users)
	} else {
		c.AbortWithStatus(http.StatusNotFound)
	}
}

// return GETS all Sounds
func getSounds(c *gin.Context) {
	sounds := DBGetSounds()
	if len(sounds) != 0 {
		c.JSON(http.StatusOK, sounds)
	} else {
		c.AbortWithStatus(http.StatusNotFound)
	}
}

// return GETS first User that matches the given UserName
func getUserByUserName(c *gin.Context) {
	user := DBGetUserByUserName(c.Param("username"))
	if user != nil {
		c.JSON(http.StatusOK, user)
	} else {
		c.AbortWithStatus(http.StatusNotFound)
	}
}

// return GETS all Sounds that match the given PosterUserName
func getSoundsByPosterUserName(c *gin.Context) {
	sounds := DBGetSoundsByPosterUserName(c.Param("posterusername"))
	if len(sounds) != 0 {
		c.JSON(http.StatusOK, sounds)
	} else {
		c.AbortWithStatus(http.StatusNotFound)
	}
}

// return POSTS a User
func postUser(c *gin.Context) {
	var usr User
	if err := c.BindJSON(&usr); err != nil {
		c.AbortWithStatus(http.StatusBadRequest)
	} else {
		DBAddUser(usr)
		c.JSON(http.StatusCreated, usr)
	}
}

// return POSTS a Sound
func postSound(c *gin.Context) {
	var snd Sound
	if err := c.BindJSON(&snd); err != nil {
		c.AbortWithStatus(http.StatusBadRequest)
	} else {
		DBAddSound(snd)
		c.JSON(http.StatusCreated, snd)
	}
}
