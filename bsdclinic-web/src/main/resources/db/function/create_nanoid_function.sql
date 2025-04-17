CREATE OR REPLACE FUNCTION generate_nanoid()
    RETURNS text AS $$
DECLARE
    nanoid text := '';
    characters text[] := array['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '_', '-'];
    i integer;
BEGIN
    FOR i IN 1..21 LOOP
            nanoid := nanoid || characters[(floor(random() * array_length(characters, 1)) + 1)];
        END LOOP;
    RETURN nanoid;
END;
$$ LANGUAGE plpgsql;
