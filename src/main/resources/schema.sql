DROP TABLE IF EXISTS event_message;
CREATE TABLE event_message (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  event_id INT NOT NULL,
  type VARCHAR(100) NOT NULL,
  data_schema VARCHAR(200) NOT NULL,
  created_at VARCHAR(100),
  tenant_id VARCHAR(100),
  payload BLOB NOT NULL,
  category VARCHAR(100) NOT NULL);