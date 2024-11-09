package api

import (
	"github.com/labstack/echo/v4"
	"github.com/sriganeshres/WorkHub-Pro/Backend/Workhub_service/database"
)

type Config struct {
	Router *echo.Echo
	Db     *database.Database
}

func (app *Config) Routes() {
	app.Router.POST("/api/workhub", app.CreateWorkHub)
	app.Router.POST("/api/createproject", app.CreateProject)

	app.Router.GET("/api/getworkhub/:id", app.GetWorkHubById)
	app.Router.DELETE("/api/workhub/:code", app.DeleteWorkHub)
	app.Router.GET("/api/Project/:id", app.GetProject)
	app.Router.DELETE("/api/Project/:id", app.Deleteproject)
	app.Router.GET("/api/Projects/:id", app.GetAllProjects)
	app.Router.POST("/api/join", app.JoinWorkHub)
	app.Router.GET("/api/getworkhub", app.GetWorkHub)
	app.Router.POST("api/createTask", app.CreateTask)
	app.Router.POST("api/updateTask", app.UpdateTask)
	app.Router.GET("/api/gettaskProject", app.GetAllTasksByProject)
	app.Router.GET("/api/gettaskToUser", app.GetTaskAssignedToUserID)
	app.Router.GET("/api/gettaskByUser", app.GetTaskAssignedByUserID)
	app.Router.GET("/api/gettaskWorkHub", app.GetTaskByWorkHubID)
	app.Router.DELETE("/api/deletetask", app.DeleteTask)
}
