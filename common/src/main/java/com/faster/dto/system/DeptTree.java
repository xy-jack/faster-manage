package com.faster.dto.system;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class DeptTree extends TreeNode {

	private String name;

}
