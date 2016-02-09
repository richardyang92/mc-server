function code_gen(name, type, size, offset, method)
    if method == "code_gen_byte" and type == "byte" and size == 1 then
        return byte_gen_tool_fn(name, offset)
    elseif method == "code_gen_byte_bbh" and type == "byte" and size == 13 then
        return bbh_gen_tool_fn(name, offset,size)
    end
end

function byte_gen(name, type, size, offset, gen)
    if gen == "byte_gen_byte" and type == "byte" and size == 1 then
        return byte_byte_tool_fn(name)
    elseif gen == "byte_gen_byte_bbh" and type == "byte" and size == 13 then
        return byte_bbh_tool_fn(name)
    end
end

function byte_gen_tool_fn(name, offset)
    return "String str_"..name.." = CodecUtil.getByteStr(data, "..offset..");\n";
end

function bbh_gen_tool_fn(name, offset,size)
    return "byte[] bt_"..name.." = CodecUtil.sub(data, "..offset..", "..offset.." + "..size..");\n".."\t\tString str_"..name.." = CodecUtil.bytes2String(bt_"..name..");\n"
end

function byte_byte_tool_fn(name)
    -- byte b = (byte) jsonObject.get("name");
    a = "String str_"..name.." = jsonObject.getString(\""..name.."\");\n"
    a = a.."\t\tbyte byte_"..name.." = Byte.parseByte(str_"..name..");\n"
    a = a.."\t\tbyte[] bytes_"..name.." = {".."byte_"..name.."};\n"
    return a
end

function byte_bbh_tool_fn(name)
    --[[ String str_tmp = jsonObject.getString("bbh");
       byte[] bt = CodecUtil.string2Bytes(str_tmp, 0, str_tmp.length());]]
    a = "String str_"..name.." = jsonObject.getString(\""..name.."\");\n"
    a = a.."\t\tbyte[] bytes_"..name.." = CodecUtil.stringNum2Bytes(".."str_"..name..", 0, ".."str_"..name..".length());\n"
    return a
end
