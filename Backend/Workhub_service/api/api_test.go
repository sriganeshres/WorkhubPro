package api

import (
	"log"
	"net/http"
	"net/http/httptest"
	"path/filepath"
	"strings"
	"testing"

	"github.com/joho/godotenv"
	"github.com/labstack/echo/v4"
	"github.com/sriganeshres/WorkHub-Pro/Backend/Workhub_service/database"
	"github.com/stretchr/testify/assert"
)

func Setup() *Config {
	e := echo.New()
	db := database.NewDatabase()
	envPath := filepath.Join("..", "..", ".env")
	dberr := godotenv.Load(envPath)
	if dberr != nil {
        panic(dberr)
    }
	app := Config{e, db}
	err := app.Db.Init()
	print(err)
	if err != nil {
		panic(err)
	}
	err = app.Db.Migrate()
	if err != nil {
		log.Fatal(err)
	}
	app.Routes()
	return &app
}
func TestCreateWorkHub(t *testing.T) {
	app := Setup()

	//Test Case 1
	//Normal Creation
	normalBody := `{"name":"SPL pvt", "description":"SPL", "admin":"Ben Kenobi", "domain":"ht.com"}`
	normalReq := httptest.NewRequest(http.MethodPost, "/api/workhub", NewReader(normalBody))
	normalReq.Header.Set(echo.HeaderContentType, echo.MIMEApplicationJSON)
	normalBodyRec := httptest.NewRecorder()
	normalBodyctx := app.Router.NewContext(normalReq, normalBodyRec)
	err1 := app.CreateWorkHub(normalBodyctx)

	// Test Case 2
	// Dplicate
	duplicateBody := `{"name":"SPL pvt", "description":"SPL", "admin":"Ben Kenobi", "domain":"ht.com"}`
	duplicateReq := httptest.NewRequest(http.MethodPost, "/api/workhub", NewReader(duplicateBody))
	duplicateReq.Header.Set(echo.HeaderContentType, echo.MIMEApplicationJSON)
	duplicateBodyRec := httptest.NewRecorder()
	duplicateBodyctx := app.Router.NewContext(duplicateReq, duplicateBodyRec)
	err2 := app.CreateWorkHub(duplicateBodyctx)

	// Assertions
	if assert.NoError(t, err1) {
		println("no error")
	}
	if assert.Error(t, err2) {
		assert.Equal(t, "another Workhub already exists", err2.Error())
	}

}

func TestCreateProject(t *testing.T) {
	app := Setup()

	//Test Case 1
	//Normal Creation
	normalBody := `{"name":"Main Project", "description":"Main Idea", "project_leader":"Ben Kenobi", "workhub_id":3,"members":"[{}]"}`
	normalReq := httptest.NewRequest(http.MethodPost, "/api/createproject", NewReader(normalBody))
	normalReq.Header.Set(echo.HeaderContentType, echo.MIMEApplicationJSON)
	normalBodyRec := httptest.NewRecorder()
	normalBodyctx := app.Router.NewContext(normalReq, normalBodyRec)
	err1 := app.CreateProject(normalBodyctx)

	// Test Case 2
	// Dplicate
	duplicateBody := `{"name":"Main Project", "description":"Main Idea", "project_leader":"Ben Kenobi", "workhub_id":3,"members":"[{}]"}`
	duplicateReq := httptest.NewRequest(http.MethodPost, "/api/createproject", NewReader(duplicateBody))
	duplicateReq.Header.Set(echo.HeaderContentType, echo.MIMEApplicationJSON)
	duplicateBodyRec := httptest.NewRecorder()
	duplicateBodyctx := app.Router.NewContext(duplicateReq, duplicateBodyRec)
	err2 := app.CreateProject(duplicateBodyctx)

	// Assertions
	if assert.NoError(t, err1) {
		println("no error")
	}
	if assert.Error(t, err2) {
		assert.Equal(t, "another Project already exists", err2.Error())
	}
}

func NewReader(reqBody string) *strings.Reader {
	return strings.NewReader(reqBody)
}
