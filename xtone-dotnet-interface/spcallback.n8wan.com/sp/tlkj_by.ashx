﻿<%@ WebHandler Language="C#" Class="tlkj_by" %>

using System;
using System.Web;

/// <summary>
/// 通联科技 - 包月（一次同步多条数据）
/// </summary>
public class tlkj_by : n8wan.Public.Logical.BaseSPCallback
{
    System.Xml.XmlElement root;
    System.Xml.XmlElement _curEl;
    string message_type;
    System.Text.StringBuilder sb;

    protected override bool OnInit()
    {

        if (Request.TotalBytes < 10)
            return false;
        var xml = new System.Xml.XmlDocument();
        try
        {
            xml.Load(Request.InputStream);
        }
        catch { return false; }

        root = xml.DocumentElement;
        System.Xml.XmlNode node = root.SelectSingleNode("message_type");
        if (node == null)
            message_type = root.InnerText;
        sb = new System.Text.StringBuilder();
        return base.OnInit();
    }
    
    protected override void WriteError(string msg)
    {
        if (sb == null)
        {
            base.WriteError(msg);
            return;
        }
        sb.AppendLine(msg);
    }

    protected override void WriteSuccess()
    {
        if (sb == null)
        {
            base.WriteSuccess();
            return;
        }
        sb.AppendLine("OK");
    }

    protected override void StartPorcess()
    {

        System.Xml.XmlNodeList els;
        els = root.SelectNodes("data/message");
        foreach (var node in els)
        {
            Reset();
            _curEl = (System.Xml.XmlElement)node;
            base.StartPorcess();
        }

        Response.Write("0");

    }

    public override string GetParamValue(string Field)
    {
        if ("message_type".Equals(Field, StringComparison.OrdinalIgnoreCase))
            return message_type;

        if (_curEl == null)
            return null;

        var node = _curEl.SelectSingleNode(Field);
        if (node == null)
            return base.GetParamValue(Field);
        return node.InnerText;
    }

}