CREATE TABLE IF NOT EXISTS stuck_reports (
                                             id            VARCHAR(255)                NOT NULL,
                                             user_id       VARCHAR(255)                NOT NULL,
                                             username      VARCHAR(255),
                                             email         VARCHAR(255),
                                             topic_id      VARCHAR(255),
                                             sub_chunk_id  VARCHAR(255),
                                             current_phase VARCHAR(255),
                                             current_url   VARCHAR(2048),
                                             user_message  VARCHAR(2048),
                                             user_agent    VARCHAR(512),
                                             status        VARCHAR(50)                 NOT NULL DEFAULT 'NEW',
                                             admin_notes   VARCHAR(2048),
                                             created_at    TIMESTAMP WITH TIME ZONE    NOT NULL DEFAULT NOW(),
                                             updated_at    TIMESTAMP WITH TIME ZONE,
                                             PRIMARY KEY (id)
);