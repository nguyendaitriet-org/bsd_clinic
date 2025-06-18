-- Create function to normalize Vietnamese text
CREATE OR REPLACE FUNCTION normalize_vietnamese_text(input_text TEXT)
RETURNS TEXT AS $$
BEGIN
    IF input_text IS NULL THEN
        RETURN NULL;
END IF;

RETURN translate(
        translate(
                translate(
                        translate(
                                translate(
                                        translate(
                                                translate(lower(input_text),
                                                          'àáạảãâầấậẩẫăằắặẳẵ', 'aaaaaaaaaaaaaaaa'),
                                                'èéẹẻẽêềếệểễ', 'eeeeeeeeeee'),
                                        'ìíịỉĩ', 'iiiii'),
                                'òóọỏõôồốộổỗơờớợởỡ', 'ooooooooooooooooo'),
                        'ùúụủũưừứựửữ', 'uuuuuuuuuuu'),
                'ỳýỵỷỹ', 'yyyyy'),
        'đ', 'd'
       );
END;
$$ LANGUAGE plpgsql IMMUTABLE;

-- Create function for Vietnamese text search
CREATE OR REPLACE FUNCTION vietnamese_text_search(text_to_search TEXT, search_keyword TEXT)
RETURNS BOOLEAN AS $$
BEGIN
    IF text_to_search IS NULL OR search_keyword IS NULL THEN
        RETURN FALSE;
END IF;

RETURN normalize_vietnamese_text(text_to_search) LIKE '%' || normalize_vietnamese_text(search_keyword) || '%';
END;
$$ LANGUAGE plpgsql IMMUTABLE;