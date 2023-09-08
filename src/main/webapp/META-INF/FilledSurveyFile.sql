USE [ISAM]
GO

/****** Object:  Table [dbo].[SurveyFile]    Script Date: 2020/12/2 下午 07:53:14 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[FilledSurveyFile](
	[id] [int] NOT NULL,
	[year] [int] NOT NULL,
	[type] [nvarchar](30) NULL,
	[file_title] [nvarchar](512) NULL,
	[file_content] [image] NULL,
	[disable] [bit] NULL,
	[updateTime] [smalldatetime] NULL
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO

