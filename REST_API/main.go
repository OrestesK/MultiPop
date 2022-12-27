package main

import (
	"github.com/gin-gonic/gin"
	"net/http"
)

// main function
func main() {
	router := gin.Default()

	router.GET("/users", getUsers)
	router.GET("/users/:username", getUserByUserName)
	router.POST("/users", postUser)

	router.GET("/sounds", getSounds)
	router.GET("/sounds/:posterusername", getSoundsByPosterUserName)
	router.POST("/sounds", postSound)

	router.Run("0.0.0.0:8080")
}

// return GETS all Users
func getUsers(c *gin.Context) {
	users := DBGetUsers()
	if users != nil || len(users) != 0 {
		c.IndentedJSON(http.StatusOK, users)
	} else {
		c.AbortWithStatus(http.StatusNotFound)
	}
}

// return GETS all Sounds
func getSounds(c *gin.Context) {
	sounds := DBGetSounds()
	if sounds != nil || len(sounds) != 0 {
		c.IndentedJSON(http.StatusOK, sounds)
	} else {
		c.AbortWithStatus(http.StatusNotFound)
	}
}

// return GETS first User that matches the given UserName
func getUserByUserName(c *gin.Context) {
	user := DBGetUserByUserName(c.Param("username"))
	if user != nil {
		c.IndentedJSON(http.StatusOK, user)
	} else {
		c.AbortWithStatus(http.StatusNotFound)
	}
}

// return GETS all Sounds that match the given PosterUserName
func getSoundsByPosterUserName(c *gin.Context) {
	sounds := DBGetSoundsByPosterUserName(c.Param("username"))
	if sounds != nil || len(sounds) == 0 {
		c.IndentedJSON(http.StatusOK, sounds)
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
		c.IndentedJSON(http.StatusCreated, usr)
	}
}

func postSound(c *gin.Context) {
	var snd Sound
	if err := c.BindJSON(&snd); err != nil {
		c.AbortWithStatus(http.StatusBadRequest)
	} else {
		DBAddSound(snd)
		c.IndentedJSON(http.StatusCreated, snd)
	}
}
