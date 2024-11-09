#!/bin/bash

# Variables
SE_DB="SE"                      # Source database name
BACKEND_DB="backend"            # Target database name
DB_USER="root"                  # Database username
DB_PASSWORD="password"          # Database password
BACKUP_FILE="/tmp/se_backup.sql" # Temporary file for backup

# Export SE database to a file with --clean flag to drop objects before creating them
PGPASSWORD=$DB_PASSWORD pg_dump -U $DB_USER -d $SE_DB --clean --if-exists -f $BACKUP_FILE

# Restore the backup to the backend database
PGPASSWORD=$DB_PASSWORD psql -U $DB_USER -d $BACKEND_DB < $BACKUP_FILE

# Remove the temporary backup file
rm $BACKUP_FILE
