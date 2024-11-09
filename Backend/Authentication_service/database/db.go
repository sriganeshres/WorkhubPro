package database

import (
	"fmt"
	"os"

	_ "github.com/lib/pq"
	"github.com/sriganeshres/WorkHub-Pro/Backend/models"
	"gorm.io/driver/postgres"
	"gorm.io/gorm"
)

type Database struct {
	DB *gorm.DB
}

func NewDatabase() *Database {
	db := &gorm.DB{}
	return &Database{DB: db}
}

func (db *Database) Init() error {
	DB, err := gorm.Open(postgres.Open(os.Getenv("DB_URL")), &gorm.Config{})
	if err != nil {
		return err
	}
	db.DB = DB
	fmt.Println("Database initialized")
	return nil
}

func (db *Database) GetUserByEmail(email string) (*models.UserData, error) {
	var user models.UserData
	err := db.DB.Where("email =?", email).First(&user).Error
	if err != nil {
		return nil, err
	}
	return &user, nil
}

func (db *Database) Migrate() error {
	err := db.DB.AutoMigrate(&models.WorkHub{}, &models.UserData{})
	if err != nil {
		return err
	}
	return nil
}

func (db *Database) CreateUser(userData *models.UserData) error {
	err := db.DB.Create(&userData).Error
	if err != nil {
		return err
	}
	return nil
}

func (db *Database) GetAllEmplyeesbyWorkhubId(workhubId uint) ([]models.UserData, error) {
	var users []models.UserData
	err := db.DB.Where("workhub_id = ? AND role = ?", workhubId, "employee").Find(&users).Error
	if err != nil {
		return nil, err
	}
	return users, nil
}

func (db *Database) GetAllProjectLeadbyWorkhubId(workhubId uint) ([]models.UserData, error) {
	var users []models.UserData
	err := db.DB.Where("workhub_id = ? AND role = ?", workhubId, "ProjectLeader").Find(&users).Error
	if err != nil {
		return nil, err
	}
	return users, nil
}
func (db *Database) GetUserByUserNameAndWorkHubID(name string, wid int) (models.UserData, error) {
	var user models.UserData
	err := db.DB.Where("username =?", name).Where("workhub_id =?", wid).First(&user).Error
	if err != nil {
		return models.UserData{}, err
	}
	return user, nil
}

func (db *Database) AddUsersToProject(userDataFromFront models.AddUsersToProject) error {
	for _, name := range userDataFromFront.Names {
		user, err := db.GetUserByUserNameAndWorkHubID(name, userDataFromFront.WorkHubID)
		if err != nil {
			return err
		}
		var ProjectID = uint(userDataFromFront.ProjectID)
		user.ProjectID = &ProjectID
		err = db.DB.Save(&user).Error
		if err != nil {
			return err
		}
	}
	return nil
}

