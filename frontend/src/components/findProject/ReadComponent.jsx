import React, { useEffect, useState } from "react";
import { getOne } from "../../api/findProjectApi";

const initState = {
  fpNo: 0,
  userId: 0,
  title: "",
  description: "",
  userSkill: [],
  userEmail: "",
  gitUrl: [],
  createdAt: "",
  updatedAt: "",
};

const ReadComponent = ({fpNo}) => {
  const [findProject, setFindProject] = useState(initState);

  useEffect(() => {
    const fetchProjectData = async () => {
      try {
        const data = await getOne(fpNo);
        console.log(data);
        setFindProject(data);
      } catch (error) {
        console.error("데이터 가져오기 실패:", error);
      }
    };
    fetchProjectData();
  }, [fpNo]);

  return (
    <div>
      <h1>{findProject.title}</h1>
      <p>{findProject.description}</p>
      <p>
        <strong>기술:</strong> {findProject.userSkill}
      </p>
      <p>
        <strong>GitHub:</strong>{" "}
        <a href={findProject.gitUrl} target="_blank" rel="noopener noreferrer">
          {findProject.gitUrl}
        </a>
      </p>
      <p>
        <strong>작성자:</strong> {findProject.userEmail}
      </p>
      <p>
        <strong>생성일:</strong> {findProject.createdAt}
      </p>
      <p>
        <strong>수정일:</strong> {findProject.updatedAt}
      </p>
    </div>
  );
};

export default ReadComponent;
