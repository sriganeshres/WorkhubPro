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

func (db *Database) Migrate() error {
	err := db.DB.AutoMigrate(&models.Message{})
	if err != nil {
		return err
	}
	return nil
}

func (db *Database) CreateMessage(Message *models.Message) error {
	err := db.DB.Create(&Message).Error
	if err != nil {
		return err
	}
	return nil
}

func (db *Database) FindChat(sender string, receiver string) (*[]models.Message, error) {
	var messages []models.Message
	err := db.DB.Where("sender = ? AND receiver = ?", sender, receiver).Find(&messages).Error
	if err != nil {
		return nil, err
	}
	return &messages, nil
}
