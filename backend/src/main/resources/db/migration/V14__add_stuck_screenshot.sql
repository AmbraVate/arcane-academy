-- Add screenshot capture to stuck reports (current_phase already exists from V11)
ALTER TABLE stuck_reports ADD COLUMN screenshot_data TEXT;
