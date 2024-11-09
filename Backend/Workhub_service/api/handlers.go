package api

import (
	// "bytes"
	"encoding/json"
	"errors"
	"fmt"
	"io"
	"net/http"
	"strconv"
	"strings"

	"github.com/labstack/echo/v4"
	"github.com/sriganeshres/WorkHub-Pro/Backend/Workhub_service/utils"
	"github.com/sriganeshres/WorkHub-Pro/Backend/models"
)

func (app *Config) CreateWorkHub(ctx echo.Context) error {
	fmt.Println("Handling POST request Workhub...")
	var WorkHub models.WorkHub
	err := ctx.Bind(&WorkHub)
	if err != nil {
		return ctx.JSON(http.StatusBadRequest, err.Error())
	}
	var Code = utils.GenerateRandomCode()
	WorkHub.PrivacyKey = Code
	if errorer := app.Db.CreateWorkhub(&WorkHub); errorer != nil {
		ctx.JSON(http.StatusBadRequest, errorer)
		return errorer
	}
	ctx.JSON(http.StatusCreated, WorkHub)
	return nil
}

func (app *Config) JoinWorkHub(ctx echo.Context) error {
	fmt.Println("Joining WorkHub...")
	var JoinData models.JoinWorkHub
	err := ctx.Bind(&JoinData)
	if err != nil {
		ctx.JSON(http.StatusBadRequest, err.Error())
		return err
	}
	key := JoinData.PrivacyKey
	email := JoinData.UserEmail
	id, err := app.VerifyKey(key)
	if err != nil {
		ctx.JSON(http.StatusBadRequest, "Invalid key")
		return errors.New("invalid key")
	}
	url := "http://localhost:8000/api/user?email=" + email // Replace with your actual server URL
	// Make GET request
	response, err := http.Get(url)
	if err != nil {
		fmt.Println("Error:", err)
		return err
	}
	defer response.Body.Close()

	// Read response body
	body, err := io.ReadAll(response.Body)
	if err != nil {
		fmt.Println("Error reading response body:", err)
		return err
	}

	// Print response body
	fmt.Println("Response:", string(body))
	var user models.UserData
	err = json.Unmarshal([]byte(body), &user)
	if err != nil {
		fmt.Println("Error parsing response body:", err)
		return err
	}
	if errorer := app.Db.UpdateWorkhub(id, user); errorer != nil {
		ctx.JSON(http.StatusBadRequest, errorer)
		return errorer
	}
	ctx.JSON(http.StatusCreated, "Successfully joined workhub")
	return nil
}

func (app *Config) Verify(ctx echo.Context) error {
	var requestData struct {
		Code int `json:"code"`
	}
	if err := ctx.Bind(&requestData); err != nil {
		return err
	}
	code := requestData.Code

	workhub, err := app.Db.FindWorkHub(code)
	if err != nil {
		return err
	}
	ctx.JSON(http.StatusCreated, workhub)
	return nil
}

// func (app *Config) CreateProject(ctx echo.Context) error {
// 	fmt.Println("Handling POST request Workhub...")
// 	var ProjectReq models.ProjectCreationRequest
// 	var Project models.Project
// 	var usersData models.AddUsersToProject
// 	err := ctx.Bind(&ProjectReq)
// 	if err != nil {
// 		fmt.Println("error in bindin")
// 		return ctx.JSON(http.StatusBadGateway, echo.Map{
// 			"success": false,
// 			"msg":     "Parsing error",
// 		})
// 	}
// 	Project.ProjectID = utils.GenerateProjectCode()
// 	Project.Name = ProjectReq.Name
// 	Project.Description = ProjectReq.Description
// 	Project.ProjectLead = ProjectReq.ProjectLead
// 	Project.WorkhubID = ProjectReq.WorkhubID
// 	usersData.Names = ProjectReq.Members
// 	usersData.ProjectID = Project.ProjectID
// 	usersData.WorkHubID = ProjectReq.WorkhubID
// 	JSONUsers, err := json.Marshal(usersData)
// 	if err != nil {
// 		ctx.JSON(http.StatusBadRequest, echo.Map{
// 			"success": false,
// 			"msg":     "Couldnt Read JSON",
// 		})
// 	}
// 	if errorer := app.Db.CreateProject(&Project); errorer != nil {
// 		ctx.JSON(http.StatusBadRequest, echo.Map{
// 			"success": false,
// 			"msg":     "Internal Error",
// 		})
// 		return errorer
// 	}
// 	url := "http://localhost:8000/api/addUsers"

// 	response, err := http.Post(url, "application/json", bytes.NewBuffer(JSONUsers))
// 	if err != nil {
// 		fmt.Println("Error:", err)
// 		ctx.JSON(http.StatusBadRequest, echo.Map{
// 			"success": false,
// 			"msg":     "CouldnOt send Request further",
// 		})
// 		return err
// 	}
// 	defer response.Body.Close()
// 	body, err := io.ReadAll(response.Body)
// 	if err != nil {
// 		fmt.Println("Error reading response body:", err)
// 		ctx.JSON(http.StatusBadRequest, echo.Map{
// 			"success": false,
// 			"msg":     "Internal Error",
// 		})
// 		return err
// 	}
// 	// Print response body
// 	fmt.Println(string(body))
// 	ctx.JSON(http.StatusCreated, echo.Map{
// 		"success": true,
// 		"msg":     "successfully created a Project",
// 	})
// 	return nil
// }

func (app *Config) CreateProject(ctx echo.Context) error {
	fmt.Println("Handling POST request Workhub...")
	var Project models.Project
	err := ctx.Bind(&Project)
	if err != nil {
		fmt.Println("error in bindin")
		return ctx.JSON(http.StatusBadGateway, err.Error())
	}

	if errorer := app.Db.CreateProject(&Project); errorer != nil {
		ctx.JSON(http.StatusBadRequest, errorer)
		return errorer
	}
	ctx.JSON(http.StatusCreated, Project)
	if usersData.Names == nil {
		return nil
	}
	url := "http://localhost:8000/api/addUsers"

	response, err := http.Post(url, "application/json", bytes.NewBuffer(JSONUsers))
	if err != nil {
		fmt.Println("Error:", err)
		ctx.JSON(http.StatusBadRequest, echo.Map{
			"success": false,
			"msg":     "CouldnOt send Request further",
		})
		return err
	}
	defer response.Body.Close()
	body, err := io.ReadAll(response.Body)
	if err != nil {
		fmt.Println("Error reading response body:", err)
		ctx.JSON(http.StatusBadRequest, echo.Map{
			"success": false,
			"msg":     "Internal Error",
		})
		return err
	}
	// Print response body
	fmt.Println(string(body))
	ctx.JSON(http.StatusCreated, echo.Map{
		"success": true,
		"msg":     "successfully created a Project",
	})
	return nil
}


func (app *Config) GetProject(ctx echo.Context) error {
	fmt.Println("Handling GET request Workhub...")
	code := ctx.Param("id")
	if code == "" {
		return ctx.JSON(http.StatusBadRequest, "Invalid Project code")
	}
	fmt.Println("code", strings.Split(code, " "))
	projectId, err := strconv.Atoi(code)
	if err != nil {
		// Handle the error if the conversion fails
		return ctx.JSON(http.StatusBadRequest, "Invalid project code")
	}

	project, errorer := app.Db.FindProject(projectId)
	if project == nil {
		return ctx.JSON(http.StatusNotFound, "Project not found")
	}
	if errorer != nil {
		ctx.JSON(http.StatusBadRequest, errorer)
		return errorer
	}
	println(project)
	ctx.JSON(http.StatusCreated, project)
	return nil
}

func (app *Config) Deleteproject(ctx echo.Context) error {
	code := ctx.Param("id")
	ProjectId, err1 := strconv.Atoi(code)
	if err1 != nil {
		ctx.JSON(http.StatusBadRequest, err1)
		return err1
	}
	err := app.Db.DeleteProject(ProjectId)
	if err != nil {
		ctx.JSON(http.StatusBadRequest, err)
	}
	ctx.JSON(http.StatusOK, "Deleted")
	return nil
}

func (app *Config) GetAllProjects(ctx echo.Context) error {
	fmt.Println("Handling GET for all projects request Workhub...")

	code := ctx.Param("id")
	workhub_id, err1 := strconv.Atoi(code)
	fmt.Println(workhub_id)
	if err1 != nil {
		ctx.JSON(http.StatusBadRequest, err1)
		return err1
	}
	projects, errorer := app.Db.GetProjectsForWorkhub(workhub_id)
	if errorer != nil {
		ctx.JSON(http.StatusBadRequest, errorer)
		return errorer
	}
	fmt.Println(projects)
	ctx.JSON(http.StatusCreated, projects)
	return nil
}
func (app *Config) VerifyKey(key int) (uint, error) {
	WorkHub, err := app.Db.FindWorkHub(key)
	if err != nil {
		return 0, errors.New("invalid key")
	}
	fmt.Println(WorkHub.Name)
	return WorkHub.ID, nil
}

func (app *Config) GetWorkHub(ctx echo.Context) error {
	key := ctx.QueryParam("key")
	int_key, err := strconv.Atoi(key)
	if err != nil {
		ctx.JSON(http.StatusBadRequest, "Invalid key")
		return err
	}
	Workhub, err := app.Db.FindWorkHub(int_key)
	if err != nil {
		ctx.JSON(http.StatusBadRequest, "Invalid key")
		return err
	}
	ctx.JSON(http.StatusOK, strconv.Itoa(int(Workhub.ID)))
	return nil
}

func (app *Config) CreateTask(ctx echo.Context) error {
	fmt.Println("Handling POST request Workhub...")
	var Task models.Task
	err := ctx.Bind(&Task)
	if err != nil {
		return ctx.JSON(http.StatusBadRequest, err.Error())
	}
	Task.ID = uint(utils.GenerateRandomCode())
	if errorer := app.Db.CreateTask(&Task); errorer != nil {
		ctx.JSON(http.StatusBadRequest, errorer)
		return errorer
	}
	ctx.JSON(http.StatusCreated, Task)
	return nil
}

func (app *Config) DeleteWorkHub(ctx echo.Context) error {
	key, erro := strconv.Atoi(ctx.QueryParam("code"))
	if erro != nil {
		ctx.JSON(http.StatusBadRequest, erro)
		return erro
	}
	err := app.Db.DeleteWorkHub(key)
	if err != nil {
		ctx.JSON(http.StatusBadRequest, err)
	}
	ctx.JSON(http.StatusNoContent, "Deleted successfully")
	return nil
}
func (app *Config) GetWorkHubById(ctx echo.Context) error {
	fmt.Println("Handling GET workhubID request Workhub...")

	code := ctx.Param("id")
	workhub_id, err1 := strconv.Atoi(code)
	fmt.Println(workhub_id)
	fmt.Println(workhub_id)
	if err1 != nil {
		ctx.JSON(http.StatusBadRequest, err1)
		return err1
	}
	Workhub, errorer := app.Db.GetWorkhubbyID(workhub_id)
	if errorer != nil {
		ctx.JSON(http.StatusBadRequest, errorer)
		return errorer
	}
	ctx.JSON(http.StatusCreated, Workhub)
	return nil

}

func (app *Config) UpdateTask(ctx echo.Context) error {
	var task models.UpdateTask
	err := ctx.Bind(&task)
	fmt.Println("tasks->",task.ID)
	if err != nil {
		ctx.JSON(http.StatusBadRequest, err.Error())
		return err
	}
	if errorer := app.Db.UpdateTask(task.ID, task.StatusField); errorer != nil {
		ctx.JSON(http.StatusBadRequest, errorer)
		return errorer
	}
	ctx.JSON(http.StatusOK, "Updated task successfully")
	return nil
}

func (app *Config) GetAllTasksByProject(ctx echo.Context) error {
	fmt.Println("Getting")
	var id int
	v := ctx.QueryParam("id")
	fmt.Println(v)
	id, err := strconv.Atoi(v)
	if err != nil {
		ctx.JSON(http.StatusBadRequest, err.Error())
		return err
	}
	var tasks []models.Task
	errorer := app.Db.GetAllTasksByProjectID(id, &tasks)
	if errorer != nil {
		ctx.JSON(http.StatusBadRequest, errorer.Error())
		return errorer
	}
	ctx.JSON(http.StatusOK, tasks)
	return nil
}

func (app *Config) GetTaskAssignedToUserID(ctx echo.Context) error {
	var id int
	v := ctx.QueryParam("id")
	id, err := strconv.Atoi(v)
	if err != nil {
		ctx.JSON(http.StatusBadRequest, err.Error())
		return err
	}
	var tasks []models.Task
	errorer := app.Db.GetAllTasksByAssignedtoUserID(id, &tasks)
	if errorer != nil {
		return errorer
	}
	ctx.JSON(http.StatusOK, tasks)
	return nil
}

func (app *Config) GetTaskAssignedByUserID(ctx echo.Context) error {
	var id int
	v := ctx.QueryParam("id")
	id, err := strconv.Atoi(v)
	if err != nil {
		ctx.JSON(http.StatusBadRequest, err.Error())
		return err
	}
	var tasks []models.Task
	errorer := app.Db.GetAllTasksByAssignedbyUserID(id, &tasks)
	if errorer != nil {
		return errorer
	}
	ctx.JSON(http.StatusOK, tasks)
	return nil
}

func (app *Config) GetTaskByWorkHubID(ctx echo.Context) error {
	var id int
	v := ctx.QueryParam("id")
	id, err := strconv.Atoi(v)
	if err != nil {
		ctx.JSON(http.StatusBadRequest, err.Error())
		return err
	}
	var tasks []models.Task
	errorer := app.Db.GetAllTasksByWorkHubID(id, &tasks)
	if errorer != nil {
		return errorer
	}
	ctx.JSON(http.StatusOK, tasks)
	return nil
}

func (app *Config) DeleteTask(ctx echo.Context) error {
	var id int
	v := ctx.QueryParam("id")
	id, err := strconv.Atoi(v)
	if err != nil {
		ctx.JSON(http.StatusBadRequest, err.Error())
		return err
	}
	err = app.Db.DeleteTask(id)
	if err != nil {
		ctx.JSON(http.StatusBadRequest, err)
	}
	ctx.JSON(http.StatusOK, "Deleted successfully")
	return nil
}
