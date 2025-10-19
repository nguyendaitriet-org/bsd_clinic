CREATE OR REPLACE FUNCTION check_filter(
    field_value TEXT,
    filter_value TEXT
)
RETURNS BOOLEAN AS $$
BEGIN
    -- Return true if the filter is null or empty
    IF filter_value IS NULL OR trim(filter_value) = '' THEN
        RETURN TRUE;
END IF;

    -- Otherwise, do a case-insensitive LIKE comparison
RETURN LOWER(field_value) LIKE LOWER('%' || filter_value || '%');
END;
$$ LANGUAGE plpgsql IMMUTABLE;